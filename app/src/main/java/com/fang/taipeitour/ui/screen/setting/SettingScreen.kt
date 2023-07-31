package com.fang.taipeitour.ui.screen.setting

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.fang.taipeitour.model.language.Language
import com.fang.taipeitour.model.language.getLocaleString
import com.fang.taipeitour.model.language.res
import com.fang.taipeitour.ui.component.CustomImage
import com.fang.taipeitour.util.screenHeightDp
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
fun SettingScreen(settingViewModel: SettingViewModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        val set = settingViewModel.state.collectAsState()
        val settingList = remember {
            mutableStateOf(GlobalContext.get().get<SettingFlavorBehavior>().apply())
        }

        val isShow = remember {
            mutableStateOf(false)
        }

        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.padding(16.dp)) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    settingList.toString(),
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
                        isShow.value = true
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
                        isShow.value = true
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        settingList.value.forEach {
            if (settingList.value.isNotEmpty()) {
                Text(text = "More Setting")
            }
            when (it) {
                ExperientalSetting.ColorScheme -> {
                    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Theme",
                                Modifier.clickable {
                                    isShow.value = true
                                }
                            )
                        }
                    }
                }
                ExperientalSetting.Clear -> {
                    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Theme",
                                Modifier.clickable {
                                    isShow.value = true
                                }
                            )
                        }
                    }
                }
            }
        }

        val context = LocalContext.current
        if (isShow.value) {
            ThemedDialog(onDismiss = { isShow.value = false }) {
                Column {
                    repeat(Language.all.size) {
                        Card(colors = CardDefaults.elevatedCardColors(containerColor = Color.Transparent)) {
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
                                    color = Color.Black
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
                    .background(Color.Black, dialogRound)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                contentAlignment = Alignment.Center,
                content = content
            )
        }
    }
}

@Composable
fun ThemeSwitcher(
    darkTheme: Boolean = false,
    size: Dp = 150.dp,
    iconSize: Dp = size / 3,
    padding: Dp = 10.dp,
    borderWidth: Dp = 1.dp,
    parentShape: Shape = CircleShape,
    toggleShape: Shape = CircleShape,
    animationSpec: AnimationSpec<Dp> = tween(durationMillis = 300),
    onClick: () -> Unit
) {
    val offset by animateDpAsState(
        targetValue = if (darkTheme) 0.dp else size,
        animationSpec = animationSpec
    )

    Box(
        modifier = Modifier
            .width(size * 2)
            .height(size)
            .clip(shape = parentShape)
            .clickable { onClick() }
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .offset(x = offset)
                .padding(all = padding)
                .clip(shape = toggleShape)
                .background(MaterialTheme.colorScheme.primary)
        ) {}
        Row(
            modifier = Modifier
                .border(
                    border = BorderStroke(
                        width = borderWidth,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    shape = parentShape
                )
        ) {
            Box(
                modifier = Modifier.size(size),
                contentAlignment = Alignment.Center
            ) {
//                Icon(
//                    modifier = Modifier.size(iconSize),
//                    imageVector = Icons.Default.Nightlight,
//                    contentDescription = "Theme Icon",
//                    tint = if (darkTheme) MaterialTheme.colorScheme.secondaryContainer
//                    else MaterialTheme.colorScheme.primary
//                )
            }
            Box(
                modifier = Modifier.size(size),
                contentAlignment = Alignment.Center
            ) {
//                Icon(
//                    modifier = Modifier.size(iconSize),
//                    imageVector = Icons.Default.LightMode,
//                    contentDescription = "Theme Icon",
//                    tint = if (darkTheme) MaterialTheme.colorScheme.primary
//                    else MaterialTheme.colorScheme.secondaryContainer
//                )
            }
        }
    }
}
