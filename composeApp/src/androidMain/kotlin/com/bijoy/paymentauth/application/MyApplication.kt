package com.bijoy.paymentauth.application

import android.app.Application
import com.bijoy.paymentauth.config.PaymentInitializer

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        PaymentInitializer.init(this)
    }
}