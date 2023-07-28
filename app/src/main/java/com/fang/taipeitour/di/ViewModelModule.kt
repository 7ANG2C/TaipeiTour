package com.fang.taipeitour.di

import com.fang.taipeitour.ui.screen.attraction.guide.AttractionGuideViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

object ViewModelModule {

    operator fun invoke() = module {
        viewModelOf(::AttractionGuideViewModel)
    }

}