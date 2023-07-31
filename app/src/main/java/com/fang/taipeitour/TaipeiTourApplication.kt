package com.fang.taipeitour

import android.app.Application
import com.fang.taipeitour.di.DataStoreModule
import com.fang.taipeitour.di.RepositoryModule
import com.fang.taipeitour.di.ViewModelModule
import com.module.taipeitourapi.external.di.TaipeiTourServiceModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TaipeiTourApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoinApplication()
    }

    private fun startKoinApplication() {
        startKoin {
            androidContext(this@TaipeiTourApplication)
            modules(
                TaipeiTourServiceModule.invoke() +
                    ViewModelModule() +
                    DataStoreModule.invoke() +
                    RepositoryModule.invoke()
            )
        }
    }
}
