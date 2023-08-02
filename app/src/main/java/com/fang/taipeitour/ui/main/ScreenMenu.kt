package com.fang.taipeitour.ui.main

import com.fang.taipeitour.R

enum class ScreenMenu(val title: String, val icon: Int) {
    HOME("HOME", R.drawable.ic_home),
    SETTING("SETTING", R.drawable.ic_settings),
    ;

    companion object {
        val all by lazy { values().toList() }
    }
}
