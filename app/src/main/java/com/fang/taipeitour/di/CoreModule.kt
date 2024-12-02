package com.fang.taipeitour.di

import com.fang.taipeitour.data.local.UserPreferencesDataStore
import com.fang.taipeitour.flavor.PreviewFunctionFlavor
import com.fang.taipeitour.flavor.PreviewFunctionFlavorImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

object CoreModule {
    operator fun invoke() =
        module {
            singleOf(::UserPreferencesDataStore)
            singleOf(::PreviewFunctionFlavorImpl).bind<PreviewFunctionFlavor>()
        }
}
