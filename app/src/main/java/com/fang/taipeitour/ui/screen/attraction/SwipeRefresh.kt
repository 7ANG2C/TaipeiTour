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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.fang.taipeitour.ui.theme.Purple40
import com.fang.taipeitour.ui.theme.Purple80
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.lang.Float.min
import kotlin.math.roundToInt
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val Primary = Color(0xFF6200EE)
val BackgroundColor = Color(0xFF464655)
val CardColor = Color(0xFFF7EBE8)
val SwatchA = Color(0xFFFFA987)
val SwatchB = Color(0xFFD85151)
val SwatchC = Color(0xFFF2DDA4)

@ExperimentalFoundationApi
@Composable
fun App() {

    val scope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }
    val refresh = remember {
        {
            scope.launch {
                isRefreshing = true
                delay(3000)
                isRefreshing = false
            }
        }
    }

    CustomPullToRefresh(
        isRefreshing = isRefreshing,
        onRefresh = { refresh() }
    ) {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 10.dp)
        ) {
            items(100) { index ->
                ListItem(index = index)
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun App2() {

    val scope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }
    val refresh = remember {
        {
            scope.launch {
                isRefreshing = true
                delay(3000)
                isRefreshing = false
            }
        }
    }

    FancyPullToRefresh(
        isRefreshing = isRefreshing,
        onRefresh = { refresh() }
    ) {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 10.dp)
        ) {
            items(100) { index ->
                ListItem(index = index)
            }
        }
    }
}

@Composable
fun ListItem(index: Int) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {

        Box(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {


            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
            ) {
                if (index % 3 == 0)
                    Text(
                        text = "TOP RATED",

                        )
                Text(
                    text = when (index) {
                        5 -> "This is Mambo..."
                        else -> "This is $index"
                    },

                    )
                Text(
                    text = when (index) {
                        5 -> "... number five"
                        else -> "This is the body of the number $index"
                    },

                    )

            }
        }
        Box(modifier = Modifier.height(8.dp))
        Divider()

    }
}

@ExperimentalFoundationApi
@Composable
fun CustomPullToRefresh(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit,
) {


    val pullState = rememberSwipeRefreshState(isRefreshing = isRefreshing)
    var offset by remember { mutableStateOf(0) }
    val animatedOffset by animateIntAsState(
        targetValue = offset,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
    )

    val density = LocalDensity.current
    val trigger = remember { 120.dp }
    val triggerPx = remember { with(density) { trigger.toPx() } }

    val indicator = remember { 50.dp }
    val indicatorPx = remember { with(density) { indicator.toPx() } }


    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        SwipeRefresh(
            modifier = modifier,
            state = pullState,
            onRefresh = onRefresh,
            refreshTriggerDistance = trigger,
            indicator = { state, _ ->

                val willRefresh = state.indicatorOffset.roundToInt() > triggerPx
                offset = state.indicatorOffset.roundToInt() + if (willRefresh) 100 else 0

                offset = when {
                    willRefresh -> triggerPx.roundToInt() + (state.indicatorOffset.roundToInt() * .1f).roundToInt()
                    state.isRefreshing -> triggerPx.roundToInt()
                    else -> state.indicatorOffset.roundToInt()
                }

                Box(
                    modifier = Modifier
                        .offset { IntOffset(0, -indicatorPx.roundToInt()) }
                        .offset { IntOffset(y = animatedOffset, x = 0) }
                        .background(
                            color = when {
                                willRefresh -> Color.Magenta
                                state.isRefreshing -> Color.Green
                                else -> Color.DarkGray
                            },
                        )
                        .size(indicator)
                )

            }
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red)
            ) {
                content()
            }

        }
    }
}

@ExperimentalFoundationApi
@Composable
fun FancyPullToRefresh(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit,
) {

    val pullState = rememberSwipeRefreshState(isRefreshing = isRefreshing)
    var offset by remember { mutableStateOf(0) }
    val animatedOffset by animateIntAsState(
        targetValue = offset,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
    )

    val density = LocalDensity.current
    val trigger = remember { 120.dp }
    val triggerPx = remember { with(density) { trigger.toPx() } }


    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        SwipeRefresh(
            modifier = modifier,
            state = pullState,
            onRefresh = onRefresh,
            refreshTriggerDistance = trigger,
            indicator = { state, _ ->

                val willRefresh = state.indicatorOffset.roundToInt() > triggerPx
                offset = state.indicatorOffset.roundToInt() + if (willRefresh) 100 else 0

                offset = when {
                    willRefresh -> triggerPx.roundToInt() + (state.indicatorOffset.roundToInt() * .1f).roundToInt()
                    state.isRefreshing -> triggerPx.roundToInt()
                    else -> state.indicatorOffset.roundToInt()
                }
            }
        ) {
            Box(
                Modifier
                    .background(color = Purple80)

            ) {

                FancyRefreshAnimation(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth(),
                    isRefreshing = { pullState.isRefreshing },
                    willRefresh = { offset > triggerPx },
                    offsetProgress = { min(animatedOffset / triggerPx, 1f) }
                )

                val scale by animateFloatAsState(
                    targetValue = if (offset > triggerPx) .95f else 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                    )
                )

                Box(modifier = Modifier
                    .scale(scale)
                    .offset { IntOffset(x = 0, y = animatedOffset) }
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .fillMaxSize()
                    .background(Purple40)
                ) {
                    content()
                }
            }
        }
    }
}

@Composable
fun FancyRefreshAnimation(
    modifier: Modifier = Modifier,
    isRefreshing: () -> Boolean,
    willRefresh: () -> Boolean,
    offsetProgress: () -> Float,
) {

    Row(
        modifier = modifier
            .padding(16.dp)
            .height(80.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f), contentAlignment = Alignment.TopCenter
        ) {
            CircleWithRing(
                modifier = Modifier
                    .size(30.dp),
                isRefreshing = isRefreshing(),
                willRefresh = willRefresh(),
                offsetProgress = offsetProgress(),
                shape = RoundedCornerShape(10.dp),
                color = SwatchB,
            )
        }

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f), contentAlignment = Alignment.BottomCenter
        ) {
            CircleWithRing(
                modifier = Modifier
                    .size(30.dp),
                isRefreshing = isRefreshing(),
                willRefresh = willRefresh(),
                offsetProgress = offsetProgress(),
                shape = RoundedCornerShape(10.dp),
                color = SwatchA,
            )
        }

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f), contentAlignment = Alignment.Center
        ) {
            CircleWithRing(
                modifier = Modifier
                    .size(30.dp),
                isRefreshing = isRefreshing(),
                willRefresh = willRefresh(),
                offsetProgress = offsetProgress(),
                shape = RoundedCornerShape(10.dp),
                color = SwatchC,
            )
        }

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f), contentAlignment = Alignment.BottomCenter
        ) {
            CircleWithRing(
                modifier = Modifier
                    .size(30.dp),
                isRefreshing = isRefreshing(),
                willRefresh = willRefresh(),
                offsetProgress = offsetProgress(),
                shape = RoundedCornerShape(10.dp),
                color = SwatchB,
            )
        }

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f), contentAlignment = Alignment.TopCenter
        ) {
            CircleWithRing(
                modifier = Modifier
                    .size(30.dp),
                isRefreshing = isRefreshing(),
                willRefresh = willRefresh(),
                offsetProgress = offsetProgress(),
                shape = RoundedCornerShape(10.dp),
                color = SwatchA,
            )
        }

    }

}

@Composable
fun CircleWithRing(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    willRefresh: Boolean,
    offsetProgress: Float,
    shape: Shape = CircleShape,
    color: Color = Color.Yellow,
) {

    val scale by animateFloatAsState(
        targetValue = if (willRefresh) 1f else .8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
        )
    )
    val rotateTransition = rememberInfiniteTransition()

    val rotation by when {
        isRefreshing -> rotateTransition.animateFloat(
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
        Box(
            modifier = Modifier
                .rotate(-rotation)
                .align(Alignment.Center)
                .scale(offsetProgress * 1.5f)
                .border(
                    width = 15.dp * (1f - offsetProgress),
                    shape = shape,
                    color = color
                )
                .fillMaxSize()
        )

        Box(
            modifier = Modifier
                .rotate(rotation)
                .align(Alignment.Center)
                .scale(offsetProgress)
                .clip(shape)
                .background(color = color)
                .fillMaxSize()
        )
    }
}