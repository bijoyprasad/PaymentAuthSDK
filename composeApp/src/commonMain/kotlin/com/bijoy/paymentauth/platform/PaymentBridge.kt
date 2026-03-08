package com.bijoy.paymentauth.platform

// expect/actual bridge so commonMain can trigger platform payment
expect fun startNativePayment(
    onSuccess: (paymentId: String) -> Unit,
    onError: (code: Int, message: String) -> Unit
)