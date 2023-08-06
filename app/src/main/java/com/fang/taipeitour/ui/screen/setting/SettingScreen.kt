package com.fang.taipeitour.ui.screen.setting

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fang.taipeitour.R
import com.fang.taipeitour.dsl.Action
import com.fang.taipeitour.dsl.Invoke
import com.fang.taipeitour.flavor.PreviewFunction
import com.fang.taipeitour.model.language.Language
import com.fang.taipeitour.model.language.getLocaleString
import com.fang.taipeitour.ui.component.CustomDialog
import com.fang.taipeitour.ui.component.TopBar
import com.fang.taipeitour.ui.component.dsl.LocalColorScheme
import com.fang.taipeitour.ui.component.dsl.LocalDarkMode
import com.fang.taipeitour.ui.component.dsl.LocalLanguage
import com.fang.taipeitour.ui.component.dsl.stateValue
import com.fang.taipeitour.ui.component.gradientBackground
import org.koin.androidx.compose.koinViewModel

/**
 * User Settings Screen
 */
@Composable
fun SettingScreen(
    viewModel: SettingViewModel = koinViewModel(),
    onMenuClicked: Invoke,
) {
    var showLanguageDialog by rememberSaveable {
        mutableStateOf(false)
    }

    // Main Content
    Column(Modifier.fillMaxSize()) {
        Crossfade(targetState = LocalLanguage) {
            TopBar(
                modifier = Modifier.fillMaxWidth(),
                text = it.getLocaleString(R.string.setting),
                onClick = onMenuClicked
            )
        }

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp, vertical = 20.dp)
        ) {
            // General
            SectionTitle(LocalLanguage.getLocaleString(R.string.general_setting))
            Spacer(modifier = Modifier.height(12.dp))
            SettingCard(
                targetState = LocalLanguage,
                scrollEffect = true,
                onClick = {
                    showLanguageDialog = true
                }
            ) {
                when (it) {
                    Language.TAIWAN -> FlagTw()
                    Language.CHINA -> FlagCn()
                    Language.ENGLISH -> FlagEn()
                    Language.JAPAN -> FlagJp()
                    Language.KOREA -> FlagKo()
                    Language.SPAN -> FlagEs()
                    Language.INDONESIA -> FlagId()
                    Language.THAILAND -> FlagTh()
                    Language.VIETNAM -> FlagVn()
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            SettingCard(
                targetState = LocalDarkMode,
                onClick = {
                    viewModel.toggleDarkMode()
                }
            ) { isDark ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .gradientBackground(
                            if (isDark) listOf(
                                Color(0xFF250C69),
                                Color(0xFF121164),
                                Color(0xFF142B69),
                            ) else listOf(
                                Color(0xFFFFF2CA),
                                Color(0xFFFFFACA),
                                Color(0xFFE5F2FF),
                            ),
                            272f
                        ),
                ) {
                    Image(
                        painter = painterResource(
                            if (isDark) R.drawable.ic_dark_mode else R.drawable.ic_light_mode
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.Center)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Preview
            viewModel.previewFunctions.stateValue().takeIf { it.isNotEmpty() }?.let { previews ->
                val title = LocalLanguage.getLocaleString(R.string.preview_function)
                SectionTitle("$title \u2728")
                Spacer(modifier = Modifier.height(12.dp))
                previews.forEach { preview ->
                    when (preview) {
                        PreviewFunction.COLOR_SCHEME -> SettingCard(
                            targetState = LocalColorScheme,
                            onClick = {
                                viewModel.toggleColorScheme()
                            }
                        ) {
                            val background: List<Color>
                            val tint: Color
                            if (LocalDarkMode) {
                                background = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.tertiary,
                                )
                                tint = MaterialTheme.colorScheme.inversePrimary
                            } else {
                                background = listOf(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                )
                                tint = MaterialTheme.colorScheme.primary
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .gradientBackground(background, 300f)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_color_scheme),
                                    contentDescription = null,
                                    tint = tint,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }

                        PreviewFunction.RESET_USER_PREFERENCE -> SettingCard(
                            targetState = viewModel.isEmpty.stateValue(),
                            onClick = {
                                viewModel.resetUserPreferences()
                            }
                        ) { isEmpty ->
                            val background: Color
                            val tint: Color
                            if ((LocalDarkMode && isEmpty) || (!LocalDarkMode && !isEmpty)) {
                                background = MaterialTheme.colorScheme.secondary
                                tint = MaterialTheme.colorScheme.inversePrimary
                            } else {
                                background = MaterialTheme.colorScheme.onSecondary
                                tint = MaterialTheme.colorScheme.primary
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(background)
                            ) {
                                Icon(
                                    painter = painterResource(
                                        if (isEmpty) R.drawable.ic_clean else R.drawable.ic_dirty
                                    ),
                                    contentDescription = null,
                                    tint = tint,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                    }
                    if (preview != previews.lastOrNull()) {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }

    // Select Language Dialog
    LanguageDialog(
        onDismissRequest = { showLanguageDialog = false },
        isShow = showLanguageDialog,
        onLanguageSelected = {
            viewModel.setLanguage(it)
        }
    )
}

@Composable
private fun SectionTitle(text: String) {
    Crossfade(targetState = text) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontSize = 12.sp)) {
                        append(" ▍")
                    }
                    withStyle(SpanStyle(fontSize = 16.sp)) {
                        append(it)
                    }
                },
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun <S> SettingCard(
    targetState: S,
    scrollEffect: Boolean = false,
    onClick: Invoke,
    content: @Composable ColumnScope.(S) -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable {
                onClick.invoke()
            }
            .aspectRatio(5f),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 1.dp
        ),
    ) {
        AnimatedContent(
            targetState = targetState,
            transitionSpec = {
                val animationSpec = tween<IntOffset>(800)
                val fadeAnimationSpec = tween<Float>(800)
                val enterTransition = if (scrollEffect) {
                    slideInVertically(animationSpec) { height ->
                        height
                    } + fadeIn(fadeAnimationSpec)
                } else {
                    fadeIn(fadeAnimationSpec)
                }
                val exitTransition = if (scrollEffect) {
                    slideOutVertically(animationSpec) { height ->
                        -height
                    } + fadeOut(fadeAnimationSpec)
                } else {
                    fadeOut(fadeAnimationSpec)
                }
                (enterTransition with exitTransition).using(SizeTransform(clip = true))
            }
        ) {
            content(it)
        }
    }
}

@Composable
private fun LanguageDialog(
    onDismissRequest: Invoke,
    isShow: Boolean,
    onLanguageSelected: Action<Language>,
) {
    if (isShow) {
        CustomDialog(onDismiss = onDismissRequest) {
            LazyColumn {
                items(Language.all) { language ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                onLanguageSelected.invoke(language)
                                onDismissRequest.invoke()
                            }
                            .background(
                                if (language == LocalLanguage) {
                                    MaterialTheme.colorScheme.secondary
                                } else {
                                    Color.Transparent
                                }
                            )
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(
                                when (language) {
                                    Language.TAIWAN -> R.drawable.flag_tw
                                    Language.CHINA -> R.drawable.flag_cn
                                    Language.ENGLISH -> R.drawable.flag_us
                                    Language.JAPAN -> R.drawable.flag_jp
                                    Language.KOREA -> R.drawable.flag_kr
                                    Language.SPAN -> R.drawable.flag_es
                                    Language.INDONESIA -> R.drawable.flag_id
                                    Language.THAILAND -> R.drawable.flag_th
                                    Language.VIETNAM -> R.drawable.flag_vn
                                }
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = language.getLocaleString(R.string.language),
                            color = if (language == LocalLanguage) {
                                MaterialTheme.colorScheme.onSecondary
                            } else {
                                MaterialTheme.colorScheme.secondary
                            },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    if (language != Language.all.lastOrNull()) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    SettingScreen {}
}
