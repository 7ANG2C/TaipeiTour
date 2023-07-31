package com.module.imageslider

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageSlider(modifier: Modifier, images: List<String>) {
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
            .clip(RoundedCornerShape(8.dp))
    ) {
        if (images.isEmpty()) {
            Text(text = "No Image")
        } else {
            val state = rememberPagerState()
            HorizontalPager(
                modifier = Modifier
                    .width(w.value.dp)
                    .height(h.value.dp)
                    .clip(RoundedCornerShape(8.dp)),
                state = state, pageCount = images.size
            ) {
                AsyncImage(
                    modifier = Modifier
                        .height(h.value.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    model = images[it],
                    contentDescription = null,
                    contentScale = ContentScale.FillHeight
                )
            }
            Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                Indicator(
                    pageCount = images.size,
                    pagerState = state,

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
    modifier: Modifier = Modifier
) {
    if (pageCount > 1) {
        Row(modifier = modifier) {
            repeat(pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color.White else Color.Gray
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }
    }
}
