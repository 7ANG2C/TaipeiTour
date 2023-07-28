package com.module.imageslider

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageSlider(modifier: Modifier, images: List<String>) {
    Box(
        modifier = modifier.clip(RoundedCornerShape(8.dp)),
    ) {
        val state = rememberPagerState()
        HorizontalPager(state = state, pageCount = images.size) {
            AsyncImage(
                modifier = Modifier.clip(RoundedCornerShape(8.dp)),
                model = images[it],
                contentDescription = null
            )
        }
        Indicator(
            pageCount = images.size,
            pagerState = state,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
        Spacer(modifier = Modifier.height(8.dp))
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