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
    private class RequestPage(val value: Int)

    private data class Mediator(
        val list: List<Attraction>,
        val state: AttractionData.State,
        val page: Int,
    )

    private companion object {
        const val FIRST_REQUEST_PAGE = 1
    }

    private val dataSizePerPage = repository.dataSizePerPage

    private val requestPageState = MutableStateFlow<RequestPage?>(null)

    private val _isRefreshingState = MutableStateFlow(false)
    val isRefreshingState = _isRefreshingState.asStateFlow()

    private val _dataState = MutableStateFlow<AttractionData?>(null)
    val dataState = _dataState.asStateFlow()

    private val _attractionState = MutableStateFlow<Attraction?>(null)
    val attractionState = _attractionState.asStateFlow()

    init {
        viewModelScope.launch {
            requestPageState.filterNotNull().flatMapLatest { page ->
                repository.invoke(page.value)
                    .flowOn(Dispatchers.IO)
                    .mapLatest {
                        Mediator(
                            list = it,
                            state = if (it.size < dataSizePerPage) {
                                AttractionData.State.SUCCESS_WITH_NO_MORE_DATA
                            } else AttractionData.State.SUCCESS,
                            page = page.value
                        )
                    }
                    .catch {
                        Mediator(
                            list = emptyList(), AttractionData.State.FAILURE, page.value
                        )
                    }
            }
                .scan(null) { acc: Mediator?, new: Mediator ->
                    if (acc == null || new.page == FIRST_REQUEST_PAGE) {
                        new
                    } else {
                        new.copy(list = acc.list + new.list)
                    }
                }
                .filterNotNull()
                .mapLatest { mediator ->
                    val loadingItem = if (mediator.state == AttractionData.State.SUCCESS) {
                        Item.Loading
                    } else null
                    AttractionData(
                        items = mediator.list.map { Item.Data(it) } + listOfNotNull(loadingItem),
                        state = mediator.state,
                        page = mediator.page
                    )
                }
                .flowOn(Dispatchers.Default)
                .collectLatest { data ->
                    setRefreshingState(false)
                    _dataState.value = data
                }
        }
        refresh()
    }

    fun refresh() {
        setRefreshingState(true)
        requestPageState.value = RequestPage(1)
    }

    fun loadMore() {
        requestPageState.update { old ->
            old?.value?.plus(1)?.let {
                RequestPage(it)
            }
        }
    }

    fun setAttractionGuide(attraction: Attraction?) {
        _attractionState.value = attraction
    }

    private fun setRefreshingState(isRefreshing: Boolean) {
        _isRefreshingState.value = isRefreshing
    }
}
