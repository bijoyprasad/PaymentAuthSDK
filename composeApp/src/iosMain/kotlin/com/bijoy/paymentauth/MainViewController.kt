package com.bijoy.paymentauth

import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = ComposeUIViewController {
    LaunchPaymentSDK(
        onPaymentSuccess = { paymentId ->
            //Toast.makeText(this, "Payment Success: $paymentId", Toast.LENGTH_LONG).show()
        },
        onPaymentError = { code, message ->
            //Log.e( "MainActivity: ", "Error")
            //Toast.makeText(this, "Payment Failed: $message", Toast.LENGTH_LONG).show()
        }
    )
}