package com.fang.taipeitour.ui.screen.attraction

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fang.taipeitour.model.OnListItemClicked
import com.fang.taipeitour.model.attraction.Attraction2
import com.module.imageslider.ImageSlider
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun AttractionScreen(
    viewModel: AttractionViewModel,
    invoke: OnListItemClicked<Attraction2>
) {

    val isRefreshing = remember {
        mutableStateOf(false)
    }
//    val pullRefreshState = rememberPullRefreshState(isRefreshing.value, {
//        isRefreshing.value = true
//        viewModel.refresh()
//    })
    val list = viewModel.state.collectAsState().value
    Surface {
        Box {
            val state = rememberLazyListState()

            Column {
//                if(isRefreshing.value) {
//                    LoadingAnimation(Modifier.fillMaxWidth().animateContentSize())
//
//                } else {
//
//                }
                FancyPullToRefresh(
                    isRefreshing = isRefreshing.value,
                    onRefresh = {
                        isRefreshing.value = true
                        viewModel.refresh()
                    }
                ) {
                    LazyColumn(
//                        modifier = Modifier.pullRefresh(pullRefreshState, !isRefreshing.value),
                        state = state,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            list?.filterIsInstance<AttractionViewModel.Item.Data>().orEmpty()
                                .map { it.sdfsdf }
                        ) { item ->
                            AttractionItem(item, invoke)
                        }
                        list?.singleOrNull { it == AttractionViewModel.Item.Hint }?.let {
                            item {
                                Text(text = "Loading\nLoading")
                            }
                        }
                    }
                }

                state.OnBottomReached {
                    viewModel.loadMore()
//                    else {
//                        coroutine.launch {
//                            delay(5.seconds)
//                            viewModel.loadMore()
//                        }
//                    }
                }
            }

            val scope = rememberCoroutineScope()
            Surface(modifier = Modifier.align(Alignment.BottomEnd)) {
                if (state.isScrollingUp()) {

                    FloatingActionButton(onClick = {
                        isRefreshing.value = false
                        scope.launch {
                            state.animateScrollToItem(0)
                        }
                    }) {
                    }
                } else {
                    list?.size?.let {
                        Text("${state.layoutInfo.visibleItemsInfo.lastOrNull()?.index}/$it")
                    }
                }
            }
        }
    }
}

@Composable
private fun AttractionItem(
    item: Attraction2,
    invoke: OnListItemClicked<Attraction2>
) {
    Column(
        Modifier.clickable {
            invoke(item)
        }
    ) {
        ImageSlider(Modifier, item.images.map { it.src })
    }
}

@Composable
private fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}

/**
 * 歷史交易下滑至底時讀取更多歷史交易資料
 */
@Composable
private fun LazyListState.OnBottomReached(
    loadMore: () -> Unit
) {
    // 判斷是否已經到底需要再讀取更多歷史交易
    val shouldLoadMore = remember {
        derivedStateOf {
            layoutInfo.visibleItemsInfo.lastOrNull()?.let { lastVisibleItem ->
                lastVisibleItem.index == layoutInfo.totalItemsCount - 1
            } ?: false
        }
    }

    LaunchedEffect(key1 = shouldLoadMore) {
        snapshotFlow { shouldLoadMore.value }.collectLatest { shouldLoadMore ->
            if (shouldLoadMore) loadMore()
        }
    }
}
