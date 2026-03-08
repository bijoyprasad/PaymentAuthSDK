package com.bijoy.paymentauth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.bijoy.paymentauth.controller.SDKController
import com.bijoy.paymentauth.ui.App

@Composable
fun PaymentSDKEntry(
    onPaymentSuccess: (paymentId: String) -> Unit = {},
    onPaymentError: (code: Int, message: String) -> Unit = { _, _ -> }
) {
    // Wire callbacks into controller
    SDKController.onPaymentSuccess = onPaymentSuccess
    SDKController.onPaymentError = onPaymentError

    val selectedImage by SDKController.selectedImage

    App(
        selectedImage = selectedImage,
        onPickImage = { SDKController.pickImageCall() },
        onDoPaymentClick = { SDKController.startBiometricCall() }
    )
}