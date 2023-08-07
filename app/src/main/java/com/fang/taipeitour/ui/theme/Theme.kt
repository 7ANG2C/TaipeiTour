package com.fang.taipeitour.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.animation.Crossfade
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.fang.taipeitour.dsl.ComposableInvoke
import com.fang.taipeitour.model.ColorScheme
import com.fang.taipeitour.ui.component.dsl.LocalColorScheme
import com.fang.taipeitour.ui.component.dsl.LocalDarkMode

@Composable
fun TaipeiTourTheme(
    darkTheme: Boolean = LocalDarkMode,
    colorScheme: ColorScheme = LocalColorScheme,
    dynamicColor: Boolean = false, // available on Android 12+
    content: ComposableInvoke
) {
    val themedColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> colorScheme.dark
        else -> colorScheme.light
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = themedColorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    Crossfade(themedColorScheme) { color ->
        MaterialTheme(
            colorScheme = color,
            typography = Typography,
            content = content
        )
    }
}
