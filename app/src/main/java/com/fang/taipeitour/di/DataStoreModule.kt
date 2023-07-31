package com.fang.taipeitour.di

import com.fang.taipeitour.datastore.user.UserPreferencesDataStore
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object DataStoreModule {

    operator fun invoke() = module {
        singleOf(::UserPreferencesDataStore)
    }
}
