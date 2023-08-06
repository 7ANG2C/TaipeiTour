package com.fang.taipeitour.di

import com.fang.taipeitour.data.local.UserPreferencesRepository
import com.fang.taipeitour.data.remote.GetAllAttractionRepository
import com.fang.taipeitour.data.remote.GetAllAttractionRepositoryImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

object RepositoryModule {

    operator fun invoke() = module {
        singleOf(::UserPreferencesRepository)
        factoryOf(::GetAllAttractionRepositoryImpl).bind<GetAllAttractionRepository>()
    }
}
