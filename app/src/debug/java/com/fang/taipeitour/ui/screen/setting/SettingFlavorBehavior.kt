package com.fang.taipeitour.ui.screen.setting

class SettingFlavorBehaviorImpl : SettingFlavorBehavior {
    override fun apply(): List<ExperientalSetting> {
        return ExperientalSetting.values().toList()
    }
}