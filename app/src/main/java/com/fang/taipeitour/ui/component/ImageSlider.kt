package com.fang.taipeitour.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.SubcomposeAsyncImage
import com.fang.taipeitour.R
import com.fang.taipeitour.dsl.Action
import com.fang.taipeitour.ui.component.dsl.screenWidthDp

/**
 * @param ratio height / width
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageSlider(
    modifier: Modifier,
    images: List<String>,
    noImageHolderRes: Int,
    contentScale: ContentScale,
    showLoading: Boolean,
    ratio: Double = 0.75,
    onPageSelect: Action<Int>,
) {
    Surface(
        modifier = modifier,
        color = Color.Transparent
    ) {
        val colorFilter = ColorFilter.colorMatrix(
            ColorMatrix(
                floatArrayOf(
                    1.25f, 0f, 0f, 0f, 0f,
                    0f, 1f, 0f, 0f, 0f,
                    0f, 0f, 0.75f, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                )
            ).apply { setToSaturation(0.6f) }
        )
        if (images.isEmpty() && noImageHolderRes != 0) {
            Image(
                painter = painterResource(noImageHolderRes),
                contentDescription = null,
                contentScale = contentScale,
                colorFilter = colorFilter
            )
        } else {
            val width = screenWidthDp.dp
            val height = (screenWidthDp * ratio).dp
            val pagerState = rememberPagerState()
            onPageSelect(pagerState.currentPage)
            HorizontalPager(
                pageCount = images.size,
                modifier = Modifier
                    .width(width)
                    .height(height),
                state = pagerState,
            ) { page ->
                val context = LocalContext.current
                SubcomposeAsyncImage(
                    model = images[page],
                    contentDescription = null,
                    imageLoader = ImageLoader.Builder(context).crossfade(true).build(),
                    modifier = Modifier
                        .width(width)
                        .height(height),
                    loading = {
                        if (showLoading) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(MaterialTheme.colorScheme.inverseOnSurface),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Loading(isFancy = false)
                            }
                        }
                    },
                    error = {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.7f)),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_error),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Oops!", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    contentScale = contentScale,
                    colorFilter = colorFilter
                )
            }
        }
    }
}
