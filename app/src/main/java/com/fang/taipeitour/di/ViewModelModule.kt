package com.fang.taipeitour.di

import com.fang.taipeitour.model.attraction.Attraction2
import com.fang.taipeitour.ui.main.MainViewModel
import com.fang.taipeitour.ui.screen.attraction.AttractionViewModel
import com.fang.taipeitour.ui.screen.attraction.guide.AttractionGuideViewModel
import com.fang.taipeitour.ui.screen.setting.SettingFlavorBehavior
import com.fang.taipeitour.ui.screen.setting.SettingFlavorBehaviorImpl
import com.fang.taipeitour.ui.screen.setting.SettingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

object ViewModelModule {

    operator fun invoke() = module {
        viewModelOf(::MainViewModel)
        viewModelOf(::AttractionViewModel)
        viewModel { (data: Attraction2) ->
            AttractionGuideViewModel(data)
        }
        viewModelOf(::SettingViewModel)
        factoryOf(::SettingFlavorBehaviorImpl).bind<SettingFlavorBehavior>()
    }
}
