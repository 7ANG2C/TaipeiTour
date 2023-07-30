package com.fang.taipeitour.ui.screen.setting

import com.fang.taipeitour.model.DarkMode
import com.fang.taipeitour.model.language.Language

data class Setting(
    val language: Language,
    val darkMode: DarkMode,
)