package com.fang.taipeitour.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fang.taipeitour.data.remote.attraction.GetAttractionListRepository
import com.fang.taipeitour.model.attraction.Attraction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 進入頁面預設 pull refresh
 */
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    private val repository: GetAttractionListRepository
) : ViewModel() {
    private class Page(val value: Int)

    private data class Mediator(
        val state: AttractionData.State,
        val list: List<Attraction>,
        val page: Int,
    )

    data class AttractionData(
        val state: State,
        val items: List<Item>,
        val page: Int,
    ) {
        enum class State {
            NoNewData, Fail, SuccessAndNew
        }

    }

    sealed class Item {
        data class Data(val attraction: Attraction) : Item()
        object Loading : Item()
    }

    private val perSize = repository.perSize

    private val pageState = MutableStateFlow<Page?>(null)

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshingState = _isRefreshing.asStateFlow()

    private val _dataState = MutableStateFlow<AttractionData?>(null)
    val dataState = _dataState.asStateFlow()

    private val _attractionState = MutableStateFlow<Attraction?>(null)
    val attractionState = _attractionState.asStateFlow()

    init {
        viewModelScope.launch {
            pageState.filterNotNull().flatMapLatest { page ->
                repository.invoke(page.value)
                    .flowOn(Dispatchers.IO)
                    .mapLatest {
                        Mediator(
                            if (it.size < perSize) {
                                AttractionData.State.NoNewData
                            } else AttractionData.State.SuccessAndNew,
                            it,
                            page.value
                        )
                    }
                    .catch {
                        Mediator(
                            AttractionData.State.Fail, emptyList(), page.value
                        )
                    }
            }
                .scan(null) { acc: Mediator?, new: Mediator ->
                    if (acc == null || new.page == 1) {
                        new
                    } else {
                        new.copy(list = acc.list + new.list)
                    }
                }
                .filterNotNull()
                .flowOn(Dispatchers.Default)
                .collectLatest { mediator ->
                    _isRefreshing.value = false
                    _dataState.value = AttractionData(
                        mediator.state,
                        when (mediator.state) {
                            AttractionData.State.NoNewData -> {
                                mediator.list.map { Item.Data(it) }
                            }
                            AttractionData.State.Fail -> {
                                mediator.list.map { Item.Data(it) }
                            }
                            AttractionData.State.SuccessAndNew -> {
                                mediator.list.map { Item.Data(it) } + Item.Loading
                            }
                        },
                        mediator.page
                    )
                }
        }
        refresh()
    }

    fun refresh() {
        _isRefreshing.value = true
        pageState.value = Page(1)
    }

    fun loadMore() {
        pageState.update { old ->
            old?.let {
                Page(it.value + 1)
            }
        }
    }

    fun setAttractionGuide(attraction: Attraction?) {
        _attractionState.value = attraction
    }
}
