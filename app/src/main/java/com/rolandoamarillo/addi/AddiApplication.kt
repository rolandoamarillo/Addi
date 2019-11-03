package com.rolandoamarillo.addi

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Addi application extension
 */
class AddiApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@AddiApplication)
            modules(environmentModule)
            modules(addiModule)
        }
    }

}