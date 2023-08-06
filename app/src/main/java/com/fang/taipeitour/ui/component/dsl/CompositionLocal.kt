package com.fang.taipeitour.ui.component.dsl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import com.fang.taipeitour.data.local.UserPreferences

val LocalStaticPreferences = staticCompositionLocalOf<UserPreferences?> { null }
val LocalPreferences @Composable get() = LocalStaticPreferences.current ?: UserPreferences.default
val LocalLanguage @Composable get() = LocalPreferences.language
val LocalDarkMode @Composable get() = LocalPreferences.darkMode.enabled
val LocalColorScheme @Composable get() = LocalPreferences.colorScheme
