package com.bijoy.paymentauth.config

import android.app.Application

object PaymentInitializer {

    internal var onPaymentSuccess: ((paymentId: String) -> Unit)? = null
    internal var onPaymentError: ((code: Int, message: String) -> Unit)? = null

    fun init(application: Application) {
        ActivityTracker.register(application)
    }

}