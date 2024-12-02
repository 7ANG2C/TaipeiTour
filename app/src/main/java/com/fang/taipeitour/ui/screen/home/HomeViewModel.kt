package com.fang.taipeitour.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fang.taipeitour.data.local.UserPreferencesRepository
import com.fang.taipeitour.data.remote.GetAllAttractionRepository
import com.fang.taipeitour.model.attraction.Attraction
import com.fang.taipeitour.model.language.Language
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: GetAllAttractionRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
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

    private val requestPageState = MutableStateFlow<RequestPage?>(null)

    private val _isRefreshingState = MutableStateFlow(false)
    val isRefreshingState = _isRefreshingState.asStateFlow()

    private val _dataState = MutableStateFlow<AttractionData?>(null)
    val dataState = _dataState.asStateFlow()

    private val _workState = MutableStateFlow<WorkState>(WorkState.Pending)
    val workState = _workState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                requestPageState.filterNotNull(),
                userPreferencesRepository.getLanguage().onEach {
                    refresh()
                },
                ::Pair,
            )
                // 過濾掉切換語言時，拿到舊的 page
                .scan(null) { acc: Pair<RequestPage, Language>?, new: Pair<RequestPage, Language> ->
                    val accLanguage = acc?.second
                    val (newPage, newLanguage) = new
                    if (newLanguage != accLanguage && newPage.value != FIRST_REQUEST_PAGE) {
                        null
                    } else {
                        new
                    }
                }
                // 主要取資料
                .mapNotNull { pair ->
                    pair?.let { (page, language) ->
                        repository.invoke(page.value, language).fold(
                            onSuccess = {
                                Mediator(
                                    list = it,
                                    state =
                                        if (it.isEmpty()) {
                                            AttractionData.State.NoMoreData
                                        } else {
                                            AttractionData.State.MightBeMoreData
                                        },
                                    page = page.value,
                                )
                            },
                            onFailure = {
                                Mediator(
                                    list = emptyList(),
                                    state = AttractionData.State.Error(it),
                                    page = page.value,
                                )
                            },
                        )
                    }
                }
                .flowOn(Dispatchers.IO)
                // 上滑取更多之列表疊加
                .scan(null) { acc: Mediator?, new: Mediator ->
                    when {
                        new.state is AttractionData.State.Error -> {
                            new.copy(list = acc?.list.orEmpty())
                        }
                        // first refresh || change language
                        acc == null || new.page == FIRST_REQUEST_PAGE -> new
                        else -> new.copy(list = acc.list + new.list)
                    }
                }
                // 是否需增加上滑取更多的 loading item
                .mapNotNull { mediator ->
                    mediator?.let {
                        val loadingItem =
                            if (mediator.state == AttractionData.State.MightBeMoreData) {
                                Item.Loading
                            } else {
                                null
                            }
                        AttractionData(
                            items = mediator.list.map { Item.Data(it) } + listOfNotNull(loadingItem),
                            state = mediator.state,
                        )
                    }
                }
                .flowOn(Dispatchers.Default)
                .collectLatest { data ->
                    setRefreshingState(false)
                    val workState =
                        when (data.state) {
                            AttractionData.State.NoMoreData -> WorkState.NoMoreData
                            is AttractionData.State.Error ->
                                WorkState.Error(
                                    data.state.t,
                                )
                            else -> WorkState.Pending
                        }
                    setWorkState(workState)
                    _dataState.value = data
                }
        }
        refresh()
    }

    fun refresh() {
        setRefreshingState(true)
        setWorkState(WorkState.Pending)
        requestPageState.value = RequestPage(FIRST_REQUEST_PAGE)
    }

    fun loadMore() {
        setWorkState(WorkState.Pending)
        requestPageState.update { old ->
            old?.value?.plus(1)?.let {
                RequestPage(it)
            }
        }
    }

    /**
     * show hint
     */
    fun setWorkState(workState: WorkState) {
        _workState.value = WorkState.Pending
        _workState.value = workState
    }

    private fun setRefreshingState(isRefreshing: Boolean) {
        _isRefreshingState.value = isRefreshing
    }
}
