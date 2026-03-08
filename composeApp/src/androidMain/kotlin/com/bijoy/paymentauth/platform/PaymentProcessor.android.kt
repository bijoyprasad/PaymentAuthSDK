package com.bijoy.paymentauth.platform

import com.bijoy.paymentauth.RazorpayResultDispatcher
import com.bijoy.paymentauth.SDKActivityTracker
import com.razorpay.Checkout
import org.json.JSONObject

actual fun startPayment(
    onSuccess: (paymentId: String) -> Unit,
    onError: (code: Int, message: String) -> Unit
) {
    val activity = SDKActivityTracker.requireCurrentActivity()

    // Register callbacks so RazorpayResultDispatcher can deliver results
    RazorpayResultDispatcher.onSuccess = onSuccess
    RazorpayResultDispatcher.onError = onError

    val checkout = Checkout()
    checkout.setKeyID("rzp_test_uvBhuJZjsXdyn3")

    try {
        val options = JSONObject().apply {
            put("name", "My App")
            put("description", "Payment")
            put("amount", 500 * 100) // amount in paise
            put("currency", "INR")
            put("prefill", JSONObject().apply {
                put("email", "test@gmail.com")
                put("name", "User")
            })
        }
        checkout.open(activity, options)
    } catch (e: Exception) {
        onError(-1, e.message ?: "Unknown error")
    }
}