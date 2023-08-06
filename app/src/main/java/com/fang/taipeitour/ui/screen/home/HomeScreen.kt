package com.fang.taipeitour.ui.screen.home

import androidx.annotation.DrawableRes
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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fang.taipeitour.R
import com.fang.taipeitour.dsl.Action
import com.fang.taipeitour.dsl.Invoke
import com.fang.taipeitour.model.attraction.Attraction
import com.fang.taipeitour.model.language.getLocaleString
import com.fang.taipeitour.ui.component.AutoSizeText
import com.fang.taipeitour.ui.component.FragmentContainer
import com.fang.taipeitour.ui.component.ImageSlider
import com.fang.taipeitour.ui.component.Loading
import com.fang.taipeitour.ui.component.PullRefresh
import com.fang.taipeitour.ui.component.TopBar
import com.fang.taipeitour.ui.component.dsl.LocalLanguage
import com.fang.taipeitour.ui.component.dsl.stateValue
import com.fang.taipeitour.ui.screen.home.attraction.AttractionArgument
import com.fang.taipeitour.ui.screen.home.attraction.AttractionFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

/**
 * all attractions screen
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onMenuClicked: Invoke,
) {
    var redirectAttraction by rememberSaveable {
        mutableStateOf<AttractionArgument?>(null)
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopBar(
            modifier = Modifier.fillMaxWidth(),
            text = LocalLanguage.getLocaleString(R.string.home),
            onClick = onMenuClicked
        )
        Box(modifier = Modifier.weight(1f)) {
            val data = viewModel.dataState.stateValue()
            val isRefreshing = viewModel.isRefreshingState.stateValue()

            // LazyColumn
            val lazyColumnState = rememberLazyListState()
            PullRefresh(
                isRefreshing = isRefreshing,
                onRefresh = { viewModel.refresh() }
            ) {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 14.dp),
                    state = lazyColumnState,
                ) {
                    itemsIndexed(data?.attractions.orEmpty()) { index, item ->
                        Column {
                            if (index == 0) {
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                            val holders = getNoImageHolderRes()
                            val noImageHolder = holders[index % holders.size]
                            AttractionItem(
                                item = item,
                                noImageHolderRes = noImageHolder
                            ) {
                                redirectAttraction = AttractionArgument(it, noImageHolder)
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                    data?.loadingItem?.let {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Loading(isFancy = false)
                            }
                        }
                    }
                }
            }
            lazyColumnState.ReachBottom {
                viewModel.loadMore()
            }

            // FloatingActionButton
            if (data?.attractions?.isNotEmpty() == true) {
                val scope = rememberCoroutineScope()
                val isScrollUp = lazyColumnState.isScrollUp()
                val visibleIndex by remember(lazyColumnState) {
                    derivedStateOf { lazyColumnState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
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
                        .padding(20.dp)
                        .align(Alignment.BottomEnd),
                    containerColor = MaterialTheme.colorScheme.tertiary
                ) {
                    AnimatedContent(
                        targetState = isScrollUp && data.items.isNotEmpty(),
                        transitionSpec = {
                            val animationSpec = tween<IntOffset>(800)
                            val fadeAnimationSpec = tween<Float>(800)
                            val enterTransition =
                                slideInVertically(animationSpec) { height ->
                                    height
                                } + fadeIn(fadeAnimationSpec)
                            val exitTransition =
                                slideOutVertically(animationSpec) { height ->
                                    -height
                                } + fadeOut(fadeAnimationSpec)
                            (enterTransition with exitTransition).using(SizeTransform(clip = true))
                        }
                    ) { isScrollUp ->
                        if (isScrollUp) {
                            Icon(
                                painter = painterResource(R.drawable.ic_scroll_up),
                                contentDescription = null,
                                modifier = Modifier.wrapContentSize(),
                                tint = MaterialTheme.colorScheme.onTertiary
                            )
                        } else {
                            Column(
                                modifier = Modifier.wrapContentSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                visibleIndex?.let {
                                    Text(
                                        text = "$it",
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

                                Text(
                                    "${data.attractions.size}",
                                    color = MaterialTheme.colorScheme.onTertiary,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }

            // Snackbar
            val workState = viewModel.workState.stateValue()
            if (workState != WorkState.Pending) {
                Snackbar(
                    Modifier
                        .align(Alignment.BottomCenter)
                        .padding(10.dp),
                    dismissAction = {
                        IconButton(
                            onClick = {
                                viewModel.setWorkState(WorkState.Pending)
                            },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_close),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.surface
                            )
                        }
                    }
                ) {
                    when (workState) {
                        is WorkState.Error -> R.string.get_attractions_error
                        WorkState.NoMoreData -> R.string.no_more_attraction
                        else -> null
                    }?.let {
                        Text(
                            text = LocalLanguage.getLocaleString(it),
                            color = MaterialTheme.colorScheme.surface,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            // Empty Attraction
            if (
                !isRefreshing && data != null &&
                !data.isError && data.attractions.isEmpty()
            ) {
                Text(
                    text = LocalLanguage.getLocaleString(R.string.empty_attraction),
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 16.sp
                )
            }

            // Error Retry
            if (
                !isRefreshing && data != null &&
                data.isError && data.attractions.isEmpty()
            ) {
                OutlinedButton(
                    onClick = { viewModel.refresh() },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 48.dp),
                ) {
                    Text(
                        text = LocalLanguage.getLocaleString(R.string.retry),
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }

    val scale by animateFloatAsState(
        targetValue = if (redirectAttraction != null) 1f else .9f,
        tween(400)
    )
    Crossfade(
        targetState = redirectAttraction,
        animationSpec = tween(400)
    ) { _attraction ->
        _attraction?.let { argument ->
            FragmentContainer(
                modifier = Modifier
                    .fillMaxSize()
                    .scale(scale),
                fragment = AttractionFragment.newInstance(argument)
                    .apply {
                        (this as AttractionFragment).onDismiss = {
                            redirectAttraction = null
                        }
                    },
                update = { /* no need update here */ }
            )
        }
    }
}

private fun getNoImageHolderRes() = listOf(
    R.drawable.no_image_holder1,
    R.drawable.no_image_holder2,
    R.drawable.no_image_holder3,
    R.drawable.no_image_holder4,
    R.drawable.no_image_holder5,
    R.drawable.no_image_holder6,
    R.drawable.no_image_holder7,
    R.drawable.no_image_holder8,
    R.drawable.no_image_holder9,
)

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
        val width = (size * count) + (space * (count - 1))
        val state = rememberLazyListState()
        LazyRow(state = state, modifier = modifier.widthIn(max = width.dp)) {
            items(pageCount) {
                val alpha = if (currentPage == it) 1f else 0.6f
                Box(
                    modifier = Modifier
                        .size(size.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = alpha))
                )
                if (it != pageCount - 1) {
                    Spacer(modifier = Modifier.width(space.dp))
                }
            }
        }
        val scope = rememberCoroutineScope()
        LaunchedEffect(currentPage) {
            scope.launch {
                val midIndex = currentPage - ((count - 1) / 2)
                state.animateScrollToItem(midIndex.coerceAtLeast(0))
            }
        }
    }
}

@Composable
private fun AttractionItem(
    item: Attraction,
    @DrawableRes noImageHolderRes: Int,
    invoke: Action<Attraction>
) {
    var isExpand by rememberSaveable { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpand) 180f else 0f
    )
    ElevatedCard(
        modifier = Modifier
            .clip(MaterialTheme.shapes.large)
            .clickable {
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
            var selectedPage by rememberSaveable {
                mutableStateOf(0)
            }
            ImageSlider(
                modifier = Modifier
                    .fillMaxSize(),
                images = item.images.map { it.src },
                noImageHolderRes = noImageHolderRes,
                contentScale = ContentScale.FillWidth,
                showLoading = false
            ) {
                selectedPage = it
            }
            Indicator(
                Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.BottomCenter),
                item.images.size,
                selectedPage
            )
        }

        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            AutoSizeText(
                text = item.name,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                targetFontSize = 16.sp,
                minFontSize = 6.sp,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
            IconButton(
                onClick = {
                    isExpand = !isExpand
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(
                    modifier = Modifier.rotate(rotationAngle),
                    painter = painterResource(R.drawable.ic_arrow_drop),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            AnimatedVisibility(visible = isExpand) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_location),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        Text(
                            text = "${item.zipCode} ${item.distric}",
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontSize = 14.sp,
                        )
                    }
                    Spacer(modifier = Modifier.padding(4.dp))
                    Row {
                        Icon(
                            painter = painterResource(R.drawable.ic_introduction),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        Text(
                            text = item.introduction,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontSize = 14.sp,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 2
                        )
                    }
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
    var preIndex by rememberSaveable(this) { mutableStateOf(firstVisibleItemIndex) }
    var preScrollOffset by rememberSaveable(this) { mutableStateOf(firstVisibleItemScrollOffset) }
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

@Preview(showBackground = true)
@Composable
private fun Preview() {
    HomeScreen {}
}
