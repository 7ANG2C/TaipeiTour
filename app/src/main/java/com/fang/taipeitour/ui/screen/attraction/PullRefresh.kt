package com.fang.taipeitour.ui.screen.attraction

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import java.lang.Float.min
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun PullRefresh(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit,
) {
    val pullState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh)
    var offset by remember { mutableStateOf(0) }
    val animatedOffset by animateIntAsState(
        targetValue = offset,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
    )

    val density = LocalDensity.current
    val trigger = remember { 60.dp }
    val triggerPx = remember { with(density) { trigger.toPx() } }

    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        Box(modifier = modifier
            .fillMaxWidth()
            .pullRefresh(pullState, !isRefreshing)) {
            PullRefreshAnimation(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth(),
                isRefreshing = { isRefreshing },
                willRefresh = { offset > triggerPx },
                offsetProgress = { min(animatedOffset / triggerPx, 1f) }
            )
            val willRefresh = pullState.progress > 0

            offset = when {
                willRefresh || isRefreshing -> triggerPx.roundToInt()
                else -> 0
            }
            val scale by animateFloatAsState(
                targetValue = if (willRefresh) .95f else 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                )
            )

            Box(
                modifier = Modifier
                    .scale(scale)
                    .offset { IntOffset(x = 0, y = animatedOffset) }
                    .fillMaxSize()
            ) {
                content()
            }
        }
    }
}

@Composable
private fun PullRefreshAnimation(
    modifier: Modifier = Modifier,
    isRefreshing: () -> Boolean,
    willRefresh: () -> Boolean,
    offsetProgress: () -> Float,
) {
    Box(
        modifier = modifier.height(60.dp),
    ) {
        Box(
            modifier = Modifier.size(30.dp),
            contentAlignment = Alignment.Center
        ) {
            val scale by animateFloatAsState(
                targetValue = if (willRefresh()) 1f else .8f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioHighBouncy,
                )
            )
            val rotateTransition = rememberInfiniteTransition()

            val rotation by when {
                isRefreshing() -> rotateTransition.animateFloat(
                    initialValue = 45f,
                    targetValue = 180f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse,
                    )
                )
                else -> remember { mutableStateOf(45f) }
            }

            Box(modifier = modifier.scale(scale)) {
                val color = MaterialTheme.colorScheme.inversePrimary
                val shape = RoundedCornerShape(10.dp)
                Box(
                    modifier = Modifier
                        .rotate(-rotation)
                        .align(Alignment.Center)
                        .scale(offsetProgress() * 1.5f)
                        .border(
                            width = 15.dp * (1f - offsetProgress()),
                            shape = shape,
                            color = color
                        )
                        .fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .rotate(rotation)
                        .align(Alignment.Center)
                        .scale(offsetProgress())
                        .clip(shape)
                        .background(color = color)
                        .fillMaxSize()
                )
            }
        }
    }
}
