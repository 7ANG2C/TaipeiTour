package com.fang.taipeitour.di

import com.fang.taipeitour.model.attraction.Attraction
import com.fang.taipeitour.ui.main.MainViewModel
import com.fang.taipeitour.ui.screen.attraction.AttractionViewModel
import com.fang.taipeitour.ui.screen.attraction.guide.AttractionGuideViewModel
import com.fang.taipeitour.ui.screen.setting.SettingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

object ViewModelModule {

    operator fun invoke(): Module = module {
        viewModelOf(::MainViewModel)
        viewModelOf(::AttractionViewModel)
        viewModel { (data: Attraction) ->
            AttractionGuideViewModel(data)
        }
        viewModelOf(::SettingViewModel)

    }
}
