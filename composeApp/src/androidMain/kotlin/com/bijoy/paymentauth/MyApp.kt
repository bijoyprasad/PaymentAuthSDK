package com.bijoy.paymentauth

import android.app.Application

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        PaymentSDK.init(this)  // ← only setup needed
    }
}