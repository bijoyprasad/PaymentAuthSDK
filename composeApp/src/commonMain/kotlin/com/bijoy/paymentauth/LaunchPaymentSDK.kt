package com.bijoy.paymentauth

// commonMain
expect fun launchPaymentSDK(
    onSuccess: (paymentId: String) -> Unit,
    onError: (code: Int, message: String) -> Unit
)