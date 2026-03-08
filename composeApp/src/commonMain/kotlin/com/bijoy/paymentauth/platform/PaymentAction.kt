package com.bijoy.paymentauth.platform

expect fun paymentAction(
    onSuccess: (paymentId: String) -> Unit,
    onError: (code: Int, message: String) -> Unit
)