package com.fang.taipeitour.ui.screen.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fang.taipeitour.R
import com.fang.taipeitour.model.Action
import com.fang.taipeitour.model.Invoke
import com.fang.taipeitour.model.attraction.Attraction
import com.fang.taipeitour.model.language.getLocaleString
import com.fang.taipeitour.ui.component.FragmentContainer
import com.fang.taipeitour.ui.component.ImageSlider
import com.fang.taipeitour.ui.component.Loading
import com.fang.taipeitour.ui.component.PullRefresh
import com.fang.taipeitour.ui.component.TopBar
import com.fang.taipeitour.ui.component.dsl.LocalLanguage
import com.fang.taipeitour.ui.component.dsl.stateValue
import com.fang.taipeitour.ui.component.noImageHolderRes
import com.fang.taipeitour.ui.screen.home.attraction.A
import com.fang.taipeitour.ui.screen.home.attraction.AttractionFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

/**
 * 全部景點 Screen
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
    onClick: Invoke
) {
    Column {
        TopBar(Modifier, text = LocalLanguage.current.getLocaleString(res = R.string.home), onClick)
        Box(modifier) {
            val isRefreshing = viewModel.isRefreshingState.stateValue()
            val data = viewModel.dataState.stateValue()

            val lazyColumnState = rememberLazyListState()
            PullRefresh(
                isRefreshing = isRefreshing,
                onRefresh = { viewModel.refresh() }
            ) {
                LazyColumn(
                    modifier = Modifier.padding(12.dp),
                    state = lazyColumnState,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        data?.attractions.orEmpty()
                    ) { item ->
                        AttractionItem(item) {
                            viewModel.setAttractionGuide(it)
                        }
                    }
                    data?.loadingItem?.let {
                        item {
                            Loading(isFancy = true)
                        }
                    }
                }
            }

            lazyColumnState.ReachBottom {
                viewModel.loadMore()
            }

            val scope = rememberCoroutineScope()
            val isScrollUp = lazyColumnState.isScrollUp()
            val sfdsfd =   remember {
                mutableStateOf( lazyColumnState.layoutInfo)
            }
            FloatingActionButton(
                onClick = {
                    if (isScrollUp) {
                        scope.launch {
                            lazyColumnState.animateScrollToItem(0)
                        }
                    }
                },
                modifier = Modifier
                    .padding(18.dp)
                    .align(Alignment.BottomEnd),
                containerColor = MaterialTheme.colorScheme.tertiary
            ) {
                AnimatedContent(
                    targetState = isScrollUp && !data?.items.isNullOrEmpty(),
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
                ) { isScrollUp ->
                    if (isScrollUp) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_scroll_up),
                            contentDescription = null,
                            modifier = Modifier.wrapContentSize(),
                            tint = MaterialTheme.colorScheme.onTertiary
                        )
                    } else {

                        Column(
                            modifier = Modifier.wrapContentSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            sfdsfd.value.visibleItemsInfo.lastOrNull()?.index?.let {
                                Text(
                                    it.toString(),
                                    color = MaterialTheme.colorScheme.onTertiary,
                                    fontSize = 12.sp
                                )
                            }
                            Divider(
                                Modifier
                                    .height(1.dp)
                                    .width(24.dp),
                                color = MaterialTheme.colorScheme.onTertiary
                            )
                            data?.attractions?.size?.let {
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

            when (data?.state) {
                AttractionData.State.SUCCESS_WITH_NO_MORE_DATA -> Snackbar(
                    Modifier.align(
                        Alignment.BottomCenter
                    )
                ) {
                    Text(text = "Error")
                }
                AttractionData.State.FAILURE -> Snackbar(Modifier.align(Alignment.BottomCenter)) {
                    Text(text = "Error")
                }
                else -> {}
            }

        }
    }

    val attraction = viewModel.attractionState.stateValue()
    val scale by animateFloatAsState(
        targetValue = if (attraction != null) 1f else .9f,
        tween(400)
//                        animationSpec = spring(
//                            dampingRatio = Spring.DampingRatioMediumBouncy,
//                        )
    )
    Crossfade(
        targetState = attraction,
        animationSpec = tween(400)
    ) { _attraction ->
        _attraction?.let {
            FragmentContainer(
                modifier = Modifier
                    .fillMaxSize()
                    .scale(scale),
                fragment = AttractionFragment.createIntent(it)
                    .apply {
                        (this as AttractionFragment).close ={
                            viewModel.setAttractionGuide(null)
                        }
                    },
                update = { /* no need update */ }
            )
        }
    }

    if (attraction != null) {
        BackHandler {
            viewModel.setAttractionGuide(null)
        }
    }


}

@Composable
private fun Indicator(
    modifier: Modifier = Modifier,
    pageCount: Int,
    currentPage: Int,
) {
    if (pageCount > 1) {
        val size = 6
        val space = 4
        val count = 5
        val width = size * count + space * (count - 1)
        val state = rememberLazyListState()
        LazyRow(state = state, modifier = modifier.width(width.dp)) {
            items(pageCount) {
                val alpha = if (currentPage == it) 1f else 0.6f
                Box(
                    modifier = Modifier
                        .size(size.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary.copy(alpha = alpha))
                )
                if (it != pageCount - 1) {
                    Spacer(modifier = Modifier.width(space.dp))
                }
            }
        }
        val scope = rememberCoroutineScope()
        LaunchedEffect(Unit) {
            scope.launch {
                state.animateScrollToItem(currentPage)
            }
        }
    }
}

@Composable
private fun AttractionItem(
    item: Attraction,
    invoke: Action<Attraction>
) {
    var isExpand by remember { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpand) 180f else 0f
    )
    ElevatedCard(
        modifier = Modifier.clickable {
            invoke(item)
        },
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f),
        ) {
            var ppage by remember {
                mutableStateOf(0)
            }
            ImageSlider(
                modifier = Modifier
                    .fillMaxSize(),
                images = item.images.map { it.src },
                noImageRes = noImageHolderRes,
                contentScale = ContentScale.FillWidth,
                false
            ) {
                ppage = it
            }
            Indicator(
                Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.BottomCenter),
                item.images.size,
                ppage
            )
        }

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
                    tint = MaterialTheme.colorScheme.tertiary
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

/**
 * 是否向上滾動
 */
@Composable
private fun LazyListState.isScrollUp(): Boolean {
    var preIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var preScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (preIndex != firstVisibleItemIndex) {
                preIndex > firstVisibleItemIndex
            } else {
                preScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                preIndex = firstVisibleItemIndex
                preScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}

/**
 * 滑動至底部
 */
@Composable
private fun LazyListState.ReachBottom(action: Action<Unit>) {
    val isHitBtm = remember {
        derivedStateOf {
            layoutInfo.visibleItemsInfo.lastOrNull()?.let {
                it.index == layoutInfo.totalItemsCount - 1
            } ?: false
        }
    }
    LaunchedEffect(isHitBtm) {
        snapshotFlow { isHitBtm.value }.collectLatest {
            if (it) action(Unit)
        }
    }
}
