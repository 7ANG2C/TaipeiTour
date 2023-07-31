package com.module.taipeitourapi.external.di

import com.module.taipeitourapi.external.data.RetrofitProvider
import com.module.taipeitourapi.external.data.TaipeiTourApi
import com.module.taipeitourapi.internal.data.RetrofitProviderImpl
import com.module.taipeitourapi.internal.data.TaipeiTourApiImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * core module
 */
object TaipeiTourServiceModule {

    operator fun invoke(overrideModules: List<Module> = emptyList()): List<Module> {
        val module = module {
            singleOf(::RetrofitProviderImpl).bind<RetrofitProvider>()
            singleOf(::TaipeiTourApiImpl) { bind<TaipeiTourApi>() }
        }
        return listOf(module) + overrideModules
    }

}
