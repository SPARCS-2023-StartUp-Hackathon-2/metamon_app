package com.andes.metamon

import android.app.Application
import com.realworld.android.logging.Logger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MetamonApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initLogger()
    }

    private fun initLogger() {
        Logger.init()
    }
}