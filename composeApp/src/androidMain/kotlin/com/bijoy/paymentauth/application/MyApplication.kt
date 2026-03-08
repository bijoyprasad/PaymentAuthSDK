package com.bijoy.paymentauth.application

import android.app.Application
import com.bijoy.paymentauth.ActivityTracker

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ActivityTracker.register(this)
    }
}