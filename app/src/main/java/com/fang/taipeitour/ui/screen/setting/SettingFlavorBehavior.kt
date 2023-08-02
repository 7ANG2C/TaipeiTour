package com.fang.taipeitour.ui.screen.setting

interface SettingFlavorBehavior : FlavorBehavior {
    operator fun invoke(): List<ExperimentalSetting>
}
