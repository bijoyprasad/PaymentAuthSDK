package com.bijoy.paymentauth.manager

import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

// This typealias matches the @objc Swift class injected via setPaymentHandler
private var paymentHandler: ((
    onSuccess: (String) -> Unit,
    onError: (Int, String) -> Unit
) -> Unit)? = null

// Called once from Swift (iOSApp.swift) to register the RazorpayHandler bridge
fun setPaymentHandler(
    handler: (onSuccess: (String) -> Unit, onError: (Int, String) -> Unit) -> Unit
) {
    paymentHandler = handler
}

fun startNativePayment(
    onSuccess: (paymentId: String) -> Unit,
    onError: (code: Int, message: String) -> Unit
) {
    dispatch_async(dispatch_get_main_queue()) {
        val handler = paymentHandler
        if (handler != null) {
            handler(onSuccess, onError)
        } else {
            onError(-1, "Payment handler not registered. Call setPaymentHandler() from Swift.")
        }
    }
}