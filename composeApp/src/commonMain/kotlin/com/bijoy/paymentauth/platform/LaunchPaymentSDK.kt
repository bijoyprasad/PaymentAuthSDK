package com.bijoy.paymentauth.platform

// commonMain
expect fun launchPaymentSDK(
    onSuccess: (paymentId: String) -> Unit,
    onError: (code: Int, message: String) -> Unit
)