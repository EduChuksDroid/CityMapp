package com.educhuks.citymapp

import android.app.Application
import com.educhuks.citymapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CityMappApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CityMappApplication)
            modules(appModule)
        }
    }
}