package com.fang.taipeitour.di

import com.fang.taipeitour.ui.main.MainViewModel
import com.fang.taipeitour.ui.screen.home.HomeViewModel
import com.fang.taipeitour.ui.screen.home.webintroduction.WebIntroductionViewModel
import com.fang.taipeitour.ui.screen.setting.SettingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

object ViewModelModule {

    operator fun invoke() = module {
        viewModelOf(::MainViewModel)
        viewModelOf(::HomeViewModel)
        viewModel {
            WebIntroductionViewModel()
        }
        viewModelOf(::SettingViewModel)
    }
}
