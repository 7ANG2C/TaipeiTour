package com.fang.taipeitour.ui.screen.attraction

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
class AttractionViewModel(
    private val repository: GetAttractionListRepository
) : ViewModel() {
    private class Page(val value: Int)

    private data class Mediator(
        val state: ListState,
        val list: List<Attraction>,
        val page: Int,
    )

    enum class ListState {
        NoNewData, Fail, SuccessAndNew
    }

    data class Out(
        val state: ListState,
        val list: List<Item>,
        val page: Int,
    )

    sealed class Item {
        data class Data(val attraction: Attraction) : Item()
        object Loading : Item()
    }

    private val perSize = repository.perSize

    private val pageState = MutableStateFlow<Page?>(null)

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshingState = _isRefreshing.asStateFlow()

    private val _listState = MutableStateFlow<Out?>(null)
    val listState = _listState.asStateFlow()

    private val _guideState = MutableStateFlow<Attraction?>(null)
    val guideState = _guideState.asStateFlow()

    init {
        viewModelScope.launch {
            pageState.filterNotNull().flatMapLatest { page ->
                repository.invoke(page.value)
                    .flowOn(Dispatchers.IO)
                    .mapLatest {
                        Mediator(
                            if (it.size < perSize) {
                                ListState.NoNewData
                            } else ListState.SuccessAndNew, it,
                            page.value
                        )
                    }
                    .catch {
                        Mediator(
                            ListState.Fail, emptyList(), page.value
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
                    _listState.value = Out(
                        mediator.state,
                        when (mediator.state) {
                            ListState.NoNewData -> {
                                mediator.list.map { Item.Data(it) }
                            }
                            ListState.Fail -> {
                                mediator.list.map { Item.Data(it) }
                            }
                            ListState.SuccessAndNew -> {
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
        _guideState.value = attraction
    }
}
