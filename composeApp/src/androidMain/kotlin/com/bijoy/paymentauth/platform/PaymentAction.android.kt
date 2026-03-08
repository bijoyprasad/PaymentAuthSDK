package com.bijoy.paymentauth.platform

import android.content.Intent
import com.bijoy.paymentauth.ActivityTracker
import com.bijoy.paymentauth.PaymentManagerActivity
import com.bijoy.paymentauth.RazorpayResultCallback

actual fun paymentAction(
    onSuccess: (paymentId: String) -> Unit,
    onError: (code: Int, message: String) -> Unit
) {
    val context = ActivityTracker.requireCurrentActivity()

    RazorpayResultCallback.onSuccess = onSuccess
    RazorpayResultCallback.onError = onError

    val intent = Intent(context, PaymentManagerActivity::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)

}