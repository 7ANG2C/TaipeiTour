package com.fang.taipeitour.ui.main

import com.fang.taipeitour.R

enum class ScreenMenu(val title: String, val icon: Int) {
    HOME("Home", R.drawable.ic_home),
    SETTING("Setting", R.drawable.ic_settings),
    ;

    companion object {
        val all by lazy { values().toList() }
    }
}
