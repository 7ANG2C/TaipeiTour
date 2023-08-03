package com.fang.taipeitour.ui.main

import androidx.annotation.StringRes
import com.fang.taipeitour.R

enum class ScreenMenu(@StringRes val titleRes: Int, val icon: Int) {
    HOME(R.string.home, R.drawable.ic_home),
    SETTING(R.string.setting, R.drawable.ic_settings),
    ;

    companion object {
        val all by lazy { values().toList() }
    }

    val isHome get()= this == HOME
}
