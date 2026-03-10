package com.bijoy.paymentauth.platform

import com.bijoy.paymentauth.manager.startNativePayment

actual fun paymentAction(
    onSuccess: (paymentId: String) -> Unit,
    onError: (code: Int, message: String) -> Unit
) {
    startNativePayment(onSuccess, onError)
}