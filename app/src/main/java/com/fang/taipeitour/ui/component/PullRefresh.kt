package com.fang.taipeitour.ui.component

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.fang.taipeitour.dsl.ComposableInvoke
import com.fang.taipeitour.dsl.Invoke
import java.lang.Float.min
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun PullRefresh(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    onRefresh: Invoke,
    content: ComposableInvoke
) {
    val pullState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh)
    var offset by remember { mutableStateOf(0) }
    val animatedOffset by animateIntAsState(
        targetValue = offset,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
    )

    val height = 60.dp
    val density = LocalDensity.current
    val triggerPx = remember { with(density) { height.toPx() } }

    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .pullRefresh(pullState, !isRefreshing)
        ) {
            Loading(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
                    .align(Alignment.TopCenter)
                    .scale(min(animatedOffset / triggerPx, 1f) * 1f),
                isFancy = false
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
