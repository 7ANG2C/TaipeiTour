package com.fang.taipeitour.ui.screen.attraction

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fang.taipeitour.R
import com.fang.taipeitour.model.OnListItemClicked
import com.fang.taipeitour.model.attraction.Attraction
import com.fang.taipeitour.ui.component.CustomImage
import com.fang.taipeitour.ui.component.ImageSlider
import com.fang.taipeitour.ui.component.dsl.stateValue
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(

    ExperimentalAnimationApi::class
)
@Composable
fun AttractionScreen(
    viewModel: AttractionViewModel,
    invoke: OnListItemClicked<Attraction>
) {

    val isRefreshing = viewModel.isRefreshingState.stateValue()

//    val pullRefreshState = rememberPullRefreshState(isRefreshing.value, {
//        isRefreshing.value = true
//        viewModel.refresh()
//    })
    val bundle = viewModel.listState.collectAsState().value
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
                PullRefresh(
                    isRefreshing = isRefreshing,
                    onRefresh = {

                        viewModel.refresh()
                    }
                ) {
                    LazyColumn(
//                        modifier = Modifier.pullRefresh(pullRefreshState, !isRefreshing.value),
                        state = state,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            bundle?.list?.filterIsInstance<AttractionViewModel.Item.Data>()
                                .orEmpty().map { it.attraction }
                        ) { item ->
                            AttractionItem(item, invoke)
                        }
                        bundle?.list?.singleOrNull { it == AttractionViewModel.Item.Loading }?.let {
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
            FloatingActionButton(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd),
                onClick = {
                    scope.launch {
                        state.animateScrollToItem(0)
                    }
                },
//                    shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 30)),
//                    backgroundColor = MaterialTheme.colorScheme.tertiary
            ) {
                AnimatedContent(
                    targetState = state.isScrollingUp(),
                    transitionSpec = {
                        val animationSpec = tween<IntOffset>(800)
                        val fadeAnimationSpec = tween<Float>(800)
                        val enterTransition = slideInVertically(animationSpec) { height ->
                            height
                        } + fadeIn(fadeAnimationSpec)
                        val exitTransition = slideOutVertically(animationSpec) { height ->
                            -height
                        } + fadeOut(fadeAnimationSpec)
                        (enterTransition with exitTransition).using(SizeTransform(clip = true))
                    }
                ) { visible ->
                    if (visible) {
                        CustomImage(res = R.drawable.ic_scroll_up)
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            bundle?.list?.size?.let {
                                Text(
                                    "${state.layoutInfo.visibleItemsInfo.lastOrNull()?.index}",
                                    color = MaterialTheme.colorScheme.onTertiary,
                                    fontSize = 12.sp
                                )
                            }
                            Divider(
                                Modifier
                                    .height(1.dp)
                                    .width(20.dp),
                                color = MaterialTheme.colorScheme.onTertiary
                            )
                            bundle?.list?.size?.let {
                                Text(
                                    "$it",
                                    color = MaterialTheme.colorScheme.onTertiary,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }

//            Snackbar(Modifier.align(Alignment.BottomCenter)) {
//                Text(text = "Error")
//            }

        }
    }
}

@Composable
private fun AttractionItem(
    item: Attraction,
    invoke: OnListItemClicked<Attraction>
) {
    var isExpand by remember { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpand) 180f else 0f
    )
    ElevatedCard(
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.clickable {
            invoke(item)
        }
    ) {
        ImageSlider(
            Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 1f), item.images.map { it.src }, RoundedCornerShape(8.dp)
        )
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = item.name)
            IconButton(
                onClick = {
                    isExpand = !isExpand
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(
                    modifier = Modifier.rotate(rotationAngle),
                    painter = painterResource(id = R.drawable.ic_arrow_drop),
                    contentDescription = null,
                    tint = Color.White
                )
            }
            AnimatedVisibility(visible = isExpand) {
                Column {
                    Text(item.zipCode + item.distric)
                    Text(
                        item.introduction,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2
                    )
                }
            }
        }
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
