package com.bijoy.paymentauth.manager

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.bijoy.paymentauth.config.PaymentResultCallback
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

internal class PaymentManager : FragmentActivity(), PaymentResultListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startRazorpay()
    }

    private fun startRazorpay() {
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_uvBhuJZjsXdyn3")
        try {
            val options = JSONObject().apply {
                put("name", "My App")
                put("description", "Payment")
                put("amount", 500 * 100)
                put("currency", "INR")
                put("prefill", JSONObject().apply {
                    put("email", "test@gmail.com")
                    put("name", "User")
                })
            }
            checkout.open(this, options)
        } catch (e: Exception) {
            PaymentResultCallback.onErrorCallback(-1, e.message ?: "Unknown error")
            finish()
        }
    }

    override fun onPaymentSuccess(razorpayPaymentID: String?) {
        PaymentResultCallback.onSuccessCallback(razorpayPaymentID ?: "")
        finish()
    }

    override fun onPaymentError(code: Int, response: String?) {
        PaymentResultCallback.onErrorCallback(code, response ?: "Payment failed")
        finish()
    }

}