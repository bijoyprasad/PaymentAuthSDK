package com.bijoy.paymentauth.platform

expect fun startPayment(
    onSuccess: (paymentId: String) -> Unit,
    onError: (code: Int, message: String) -> Unit
)