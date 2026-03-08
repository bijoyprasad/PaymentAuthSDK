package com.bijoy.paymentauth.features

import android.app.Activity
import com.razorpay.Checkout
import org.json.JSONObject

class RazorpayManager {

    fun startPayment(
        activity: Activity,
        amount: Int,
        userName: String,
        email: String
    ) {
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_uvBhuJZjsXdyn3")
        try {
            val options = JSONObject()
            options.put("name", "My App")
            options.put("description", "Payment")
            options.put("amount", amount * 100)
            options.put("currency", "INR")
            val prefill = JSONObject()
            prefill.put("email", email)
            prefill.put("name", userName)
            options.put("prefill", prefill)
            checkout.open(activity, options)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun onSuccess(paymentId: String) {
        //PaymentSDK.notifySuccess(paymentId)
    }

    fun onFailure(error: String) {
       // PaymentSDK.notifyFailure(error)
    }
}