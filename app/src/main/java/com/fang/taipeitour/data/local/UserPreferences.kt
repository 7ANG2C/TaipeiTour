package com.fang.taipeitour.data.local

import com.fang.taipeitour.model.ColorScheme
import com.fang.taipeitour.model.DarkMode
import com.fang.taipeitour.model.language.Language

data class UserPreferences(
    val language: Language,
    val darkMode: DarkMode,
    val colorScheme: ColorScheme,
) {

    companion object {
        val default = UserPreferences(
            Language.default, DarkMode.default, ColorScheme.default
        )
    }
}
