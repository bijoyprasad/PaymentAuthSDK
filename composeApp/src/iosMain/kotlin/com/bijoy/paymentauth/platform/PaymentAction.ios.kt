package com.bijoy.paymentauth.platform

actual fun paymentAction(
    onSuccess: (paymentId: String) -> Unit,
    onError: (code: Int, message: String) -> Unit
) {
    startNativePayment(onSuccess, onError)
}