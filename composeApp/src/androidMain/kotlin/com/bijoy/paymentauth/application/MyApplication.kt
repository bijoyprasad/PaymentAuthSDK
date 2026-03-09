package com.bijoy.paymentauth.application

import android.app.Application
import com.bijoy.paymentauth.PaymentSDK

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        PaymentSDK.init(this)
    }
}