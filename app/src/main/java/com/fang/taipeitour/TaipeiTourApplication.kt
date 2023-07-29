package com.fang.taipeitour

import android.app.Application
import com.fang.taipeitour.di.DataStoreModule
import com.fang.taipeitour.di.ViewModelModule
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
                ViewModelModule(),
                DataStoreModule()
            )
        }
    }
}
