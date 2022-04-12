package com.example.loadstate

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 *
 *
 * @Author holo
 * @Date 2022/4/11
 */


val appContext: Application by lazy { StateApp.app }

@HiltAndroidApp
class StateApp : Application() {

    internal companion object {
        lateinit var app: Application
    }

    override fun onCreate() {
        super.onCreate()
        app = this
    }
}