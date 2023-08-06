package com.fang.taipeitour.model

import com.fang.taipeitour.ui.theme.color.AppleDark
import com.fang.taipeitour.ui.theme.color.AppleLight
import com.fang.taipeitour.ui.theme.color.BiscuitDark
import com.fang.taipeitour.ui.theme.color.BiscuitLight
import com.fang.taipeitour.ui.theme.color.BlueberryDark
import com.fang.taipeitour.ui.theme.color.BlueberryLight
import com.fang.taipeitour.ui.theme.color.GrapeDark
import com.fang.taipeitour.ui.theme.color.GrapeLight
import com.fang.taipeitour.ui.theme.color.TreeDark
import com.fang.taipeitour.ui.theme.color.TreeLight
import androidx.compose.material3.ColorScheme as MaterialScheme

/**
 * @property key data store storage, DO NOT modify
 */
enum class ColorScheme(val light: MaterialScheme, val dark: MaterialScheme, val key: String) {
    BLUEBERRY(BlueberryLight, BlueberryDark, "blueberry"),
    TREE(TreeLight, TreeDark, "tree"),
    GRAPE(GrapeLight, GrapeDark, "grape"),
    APPLE(AppleLight, AppleDark, "apple"),
    BISCUIT(BiscuitLight, BiscuitDark, "biscuit"),
    ;

    companion object {
        val default = BLUEBERRY
        operator fun get(key: String): ColorScheme? = all.find { it.key == key }

        private val all by lazy { values().toList() }
    }

    val next get() = all.getOrNull(ordinal + 1) ?: default
}
