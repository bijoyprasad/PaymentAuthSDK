package com.bijoy.paymentauth.platform

import android.content.Intent
import com.bijoy.paymentauth.ActivityTracker
import com.bijoy.paymentauth.PaymentManager
import com.bijoy.paymentauth.PaymentResultCallback

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