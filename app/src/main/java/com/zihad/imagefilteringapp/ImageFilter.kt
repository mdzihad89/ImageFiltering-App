package com.zihad.imagefilteringapp

import android.app.Application
import com.zihad.imagefilteringapp.di.repositoryModule
import com.zihad.imagefilteringapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Suppress("unused")
class ImageFilter : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ImageFilter)
            modules(listOf(repositoryModule, viewModelModule))
        }
    }
}