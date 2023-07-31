package com.fang.taipeitour.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageSlider(
    modifier: Modifier,
    images: List<String>,
    clip: Shape
) {
    val dd = LocalConfiguration.current
    val w = remember {
        mutableStateOf(dd.screenWidthDp * 0.9)
    }
    val h = remember {
        mutableStateOf(w.value * 0.75)
    }
    Box(
        modifier = modifier
            .width(w.value.dp)
            .height(h.value.dp)
            .clip(clip)
    ) {
        if (images.isEmpty()) {
            Text(text = "No Image")
        } else {
            val state = rememberPagerState()
            HorizontalPager(
                modifier = Modifier
                    .width(w.value.dp)
                    .height(h.value.dp)
                    .clip(clip),
                state = state, pageCount = images.size
            ) {
                var imageVisible by remember { mutableStateOf(false) }

                val imageAlpha: Float by animateFloatAsState(
                    targetValue = if (imageVisible) 1f else 0f,
                    animationSpec = tween(durationMillis = 300)
                )
//                Box(modifier =  Modifier
//                    .height(h.value.dp)
//                    .clip(RoundedCornerShape(8.dp)).alpha(imageAlpha)) {
                AsyncImage(
                    modifier = Modifier
                        .height(h.value.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    model = images[it],
                    alpha = imageAlpha,
                    contentDescription = null,
                    contentScale = ContentScale.FillHeight,
                    onSuccess = {
                        imageVisible = true
                    },
                    colorFilter = ColorFilter.colorMatrix(
                        ColorMatrix(
                            floatArrayOf(
                                1.25f, 0f, 0f, 0f, 0f,
                                0f, 1f, 0f, 0f, 0f,
                                0f, 0f, 0.75f, 0f, 0f,
                                0f, 0f, 0f, 1f, 0f
                            )
                        ).apply {
                            this.setToSaturation(0.8f)
                        }
                    )
                )
//                }

            }
            Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                Indicator(
                    pageCount = images.size,
                    pagerState = state,
                    currentPage = state.currentPage
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Indicator(
    pageCount: Int,
    pagerState: PagerState,
    currentPage: Int,
    modifier: Modifier = Modifier
) {

    if (pageCount > 1) {
        val state = rememberLazyListState()
        val sdf = rememberCoroutineScope()
        LazyRow(state = state, modifier = modifier.width(84.dp)) {
            items(pageCount) {
                val alpha = if (pagerState.currentPage == it) 1f else 0.6f
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary.copy(alpha = alpha))
                )
                if (it != pageCount - 1) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
        LaunchedEffect(key1 = currentPage, block = {

            sdf.launch {
                state.animateScrollToItem(currentPage)
            }
        })
    }


}
