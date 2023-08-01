package com.fang.taipeitour.ui.screen.setting

import android.view.MotionEvent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.fang.taipeitour.model.language.Language
import com.fang.taipeitour.model.language.getLocaleString
import com.fang.taipeitour.model.language.res
import com.fang.taipeitour.ui.component.CustomImage
import com.fang.taipeitour.ui.component.screenHeightDp
import org.koin.core.context.GlobalContext
import java.lang.Math.PI
import java.lang.Math.cos
import java.lang.Math.min
import java.lang.Math.sin
import java.lang.Math.sqrt
import kotlin.math.pow

/**
 * 使用者設定 Screen
 */
@Composable
fun SettingScreen(viewModel: SettingViewModel) {
    Column(
        modifier = Modifier
            .background(gradient)
            .padding(16.dp)
            .fillMaxSize()
    ) {

        val isShow = remember {
            mutableStateOf(false)
        }

        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.padding(16.dp)) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    viewModel.language.collectAsState().toString(),
                    Modifier.clickable {
                        isShow.value = true
                    }
                )
            }
        }
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
        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Theme",
                    Modifier.clickable {
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        AnimatedButton()
        val settingList = remember {
            mutableStateOf(GlobalContext.get().get<SettingFlavorBehavior>().invoke())
        }

        if (settingList.value.isNotEmpty()) {
            Text(text = "More Setting")
            settingList.value.forEach {

                when (it) {
                    ExperimentalSetting.ColorScheme -> {
                        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    "Theme",
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
                                    "Theme",
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
        if (isShow.value) {
            ThemedDialog(onDismiss = { isShow.value = false }) {
                Column() {
                    repeat(Language.all.size) {
                        Card(
                            modifier = Modifier.clickable {
                                viewModel.setl(Language.all[it])
                            },
                            colors = CardDefaults.elevatedCardColors(containerColor = Color.Transparent)
                        ) {
                            Row(
                                modifier = Modifier.gradientBackground(
                                    colors = listOf(
                                        Color.Red.copy(alpha = 0.2f),
                                        Color.Blue.copy(alpha = 0.2f)
                                    ),
                                    angle = 135f
                                )
                            ) {
                                CustomImage(
                                    modifier = Modifier.size(24.dp),
                                    res = Language.all[it].res
                                )
                                Text(
                                    text = Language.all[it].getLocaleString(context) ?: "??",
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AnimatedButton() {
    val selected = remember { mutableStateOf(false) }
    val scale = animateFloatAsState(if (selected.value) 1.2f else 1f)

    Column(
        Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { },
            modifier = Modifier
                .scale(scale.value)
                .height(40.dp)
                .width(200.dp)
                .pointerInteropFilter {
                    when (it.action) {
                        MotionEvent.ACTION_DOWN -> selected.value = true
                        MotionEvent.ACTION_UP -> selected.value = false
                    }
                    true
                }
        ) {
            Text(text = "Explore", fontSize = 15.sp, color = Color.White)
        }
    }
}

val gradient
    @Composable get() = Brush.linearGradient(
        0.0f to MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
        500.0f to MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f),
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
private fun ThemedDialog(onDismiss: () -> Unit, content: @Composable BoxScope.() -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }) {
        val dialogRound = RoundedCornerShape(32.dp)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(0.dp, (screenHeightDp * 0.9f).dp),
            shape = dialogRound,
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
