package com.bijoy.paymentauth

import com.bijoy.paymentauth.config.ActivityTracker
import com.bijoy.paymentauth.config.PaymentInitializer
import com.bijoy.paymentauth.manager.ImagePickManager

// androidMain
actual fun launchPaymentSDK(
    onSuccess: (paymentId: String) -> Unit,
    onError: (code: Int, message: String) -> Unit
) {
    PaymentInitializer.onPaymentSuccess = onSuccess
    PaymentInitializer.onPaymentError = onError
    val context = ActivityTracker.requireCurrentActivity()
    context.startActivity(ImagePickManager.newIntent(context))
}