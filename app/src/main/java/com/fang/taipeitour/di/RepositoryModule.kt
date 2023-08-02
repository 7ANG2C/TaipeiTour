package com.fang.taipeitour.di

import com.fang.taipeitour.data.local.UserPreferencesRepository
import com.fang.taipeitour.data.remote.attraction.GetAttractionListRepository
import com.fang.taipeitour.data.remote.attraction.GetAttractionListRepositoryImpl
import com.fang.taipeitour.ui.screen.setting.SettingFlavorBehavior
import com.fang.taipeitour.ui.screen.setting.SettingFlavorBehaviorImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

object RepositoryModule {

    operator fun invoke(): Module = module {
        singleOf(::SettingFlavorBehaviorImpl).bind<SettingFlavorBehavior>()
        singleOf(::UserPreferencesRepository)
        factoryOf(::GetAttractionListRepositoryImpl).bind<GetAttractionListRepository>()
    }
}
