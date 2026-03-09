package com.bijoy.paymentauth.platform

import android.content.Intent
import com.bijoy.paymentauth.config.ActivityTracker
import com.bijoy.paymentauth.config.PaymentResultCallback
import com.bijoy.paymentauth.manager.PaymentManager

actual fun startNativePayment(
    onSuccess: (paymentId: String) -> Unit,
    onError: (code: Int, message: String) -> Unit
) {
    val context = ActivityTracker.requireCurrentActivity()
    PaymentResultCallback.onSuccess = onSuccess
    PaymentResultCallback.onError = onError
    val intent = Intent(context, PaymentManager::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
}