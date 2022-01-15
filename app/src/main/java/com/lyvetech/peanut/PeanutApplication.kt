package com.lyvetech.peanut

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PeanutApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}