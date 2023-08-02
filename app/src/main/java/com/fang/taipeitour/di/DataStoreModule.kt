package com.fang.taipeitour.di

import com.fang.taipeitour.data.local.user.UserPreferencesDataStore
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object DataStoreModule {

    operator fun invoke(): Module = module {
        singleOf(::UserPreferencesDataStore)
    }
}
