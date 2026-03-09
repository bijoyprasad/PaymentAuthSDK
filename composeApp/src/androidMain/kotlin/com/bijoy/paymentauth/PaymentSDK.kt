package com.bijoy.paymentauth

import android.app.Application

object PaymentSDK {

    internal var onPaymentSuccess: ((paymentId: String) -> Unit)? = null
    internal var onPaymentError: ((code: Int, message: String) -> Unit)? = null

    fun init(application: Application) {
        ActivityTracker.register(application)
    }

    fun launch(
        onSuccess: (paymentId: String) -> Unit,
        onError: (code: Int, message: String) -> Unit = { _, _ -> }
    ) {
        onPaymentSuccess = onSuccess
        onPaymentError = onError

        val context = ActivityTracker.requireCurrentActivity()
        context.startActivity(LaunchManager.newIntent(context))
    }
}