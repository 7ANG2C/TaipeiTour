package com.fang.taipeitour.ui.screen.setting

import android.view.MotionEvent
import androidx.compose.animation.core.Spring.StiffnessHigh
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.fang.taipeitour.R
import com.fang.taipeitour.model.Action
import com.fang.taipeitour.model.Invoke
import com.fang.taipeitour.model.language.Language
import com.fang.taipeitour.model.language.flag
import com.fang.taipeitour.model.language.getLocaleString
import com.fang.taipeitour.ui.component.TopBar
import com.fang.taipeitour.ui.component.dsl.LocalLanguage
import com.fang.taipeitour.ui.component.screenHeightDp
import java.lang.Math.PI
import java.lang.Math.cos
import java.lang.Math.min
import java.lang.Math.sin
import java.lang.Math.sqrt
import kotlin.math.pow
import org.koin.androidx.compose.koinViewModel
import org.koin.core.context.GlobalContext

/**
 * User Settings Screen
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SettingScreen(viewModel: SettingViewModel = koinViewModel(), onClick: Invoke) {
    Column(
        modifier = Modifier
            .background(gradient)
            .fillMaxSize()
    ) {
        var isShow by remember {
            mutableStateOf(false)
        }
        val language = LocalLanguage.current
        TopBar(Modifier, text = language.getLocaleString(R.string.setting), onClick)
        Column(
            modifier = Modifier.padding(16.dp)

        ) {
            AnimatedButton({
//            if (!it) { isShow = true }
            }) {

                Row(modifier = Modifier.padding(16.dp)) {
                    Spacer(modifier = Modifier.weight(1f))
                    language?.flag?.let {
                        Image(
                            painter = painterResource(language.flag),
                            contentDescription = null,
                            Modifier.size(48.dp)
                        )
                    }
                }
            }
//        ElevatedCard(
//            modifier = Modifier
//                .fillMaxWidth()
//        ) {
//
//            Row(modifier = Modifier.padding(16.dp)) {
//                Spacer(modifier = Modifier.weight(1f))
//                language?.flag?.let {
//                    Image(
//                        painter = painterResource(language.flag),
//                        contentDescription = null,
//                        Modifier.size(48.dp)
//                    )
//                }
//            }
//        }
            Spacer(modifier = Modifier.height(16.dp))

            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "DarkMode",
                        Modifier.clickable {
                            viewModel.toggleDarkMode()
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
//        AnimatedButton()
            val settingList = remember {
                mutableStateOf(GlobalContext.get().get<SettingFlavorBehavior>().invoke())
            }

            if (settingList.value.isNotEmpty()) {
                Text(language.getLocaleString(R.string.experimental_setting))
                settingList.value.forEach {

                    when (it) {
                        ExperimentalSetting.ColorScheme -> {
                            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                                Row(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        "Color",
                                        Modifier.clickable {
                                        }
                                    )
                                }
                            }
                        }

                        ExperimentalSetting.Clear -> {
                            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                                Row(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        "clear",
                                        Modifier.clickable {
                                            viewModel.reset()
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            val context = LocalContext.current
            if (isShow) {
                ThemedDialog(onDismiss = { isShow = false }) {
                    Column() {
                        repeat(Language.all.size) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable {
                                        viewModel.setLanguage(Language[it])
                                        isShow = false
                                    }
                                    .background(MaterialTheme.colorScheme.tertiary.copy(alpha = (0.1f)))
                                    .padding(vertical = 8.dp, horizontal = 16.dp),
                            ) {

                                Image(
                                    modifier = Modifier.size(24.dp),
                                    painter = painterResource(id = Language[it].flag),
                                    contentDescription = null,
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = Language[it].getLocaleString(R.string.language),
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }

    }
}

@Composable
private fun FlagA() {
    Box(modifier = Modifier.background(Color((0xff123456)))) {
        Image(painter = painterResource(id = R.drawable.ic_star), contentDescription = null)
    }
}

@Composable
private fun FlagB() {
    Box(modifier = Modifier.background(Color((0xff123456)))) {
        Image(painter = painterResource(id = R.drawable.ic_star), contentDescription = null)
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AnimatedButton(incoke: Action<Boolean>, content: @Composable ColumnScope.() -> Unit) {
    val selected = remember { mutableStateOf<Boolean?>(null) }
    val scale = animateFloatAsState(
        if (selected.value == true) 0.9f else 1f,
        spring(stiffness = StiffnessHigh)
    )
    selected.value?.let(incoke)
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale.value)
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        selected.value = true
                    }
                    MotionEvent.ACTION_UP -> {
                        selected.value = false
                    }
                }
                true
            }
    ) {
        content()
    }
//    Column(
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Button(
//            onClick = { },
//            modifier = Modifier
//                .scale(scale.value)
//                .height(40.dp)
//                .width(200.dp)
//                .pointerInteropFilter(
//                    RequestDisallowInterceptTouchEvent(
//                    )
//                ) {
//                    when (it.action) {
//                        MotionEvent.ACTION_DOWN -> {
//                            selected.value = true
//                        }
//                        MotionEvent.ACTION_UP -> {
//                            selected.value = false
//                        }
//                    }
//                    true
//                }
//        ) {
//            Text(text = "Explore", fontSize = 15.sp, color = Color.White)
//        }
//    }
}

val gradient
    @Composable get() = Brush.linearGradient(
        0.0f to MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
        500.0f to MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f),
        start = Offset.Zero,
        end = Offset.Infinite
    )

fun Modifier.gradientBackground(colors: List<Color>, angle: Float) = this.then(
    Modifier.drawBehind {
        val angleRad = angle / 180f * PI
        val x = cos(angleRad).toFloat() // Fractional x
        val y = sin(angleRad).toFloat() // Fractional y

        val radius = sqrt((size.width.pow(2) + size.height.pow(2)).toDouble()).toFloat() / 2f
        val offset = center + Offset(x * radius, y * radius)

        val exactOffset = Offset(
            x = min(offset.x.coerceAtLeast(0f), size.width),
            y = size.height - min(offset.y.coerceAtLeast(0f), size.height)
        )

        drawRect(
            brush = Brush.linearGradient(
                colors = colors,
                start = Offset(size.width, size.height) - exactOffset,
                end = exactOffset
            ),
            size = size
        )
    }
)

@Composable
private fun ThemedDialog(onDismiss: Invoke, content: @Composable BoxScope.() -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }) {
        val dialogRound = RoundedCornerShape(32.dp)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(0.dp, (screenHeightDp * 0.9f).dp),
            shape = MaterialTheme.shapes.extraLarge,
//            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                contentAlignment = Alignment.Center,
                content = content
            )
        }
    }
}
