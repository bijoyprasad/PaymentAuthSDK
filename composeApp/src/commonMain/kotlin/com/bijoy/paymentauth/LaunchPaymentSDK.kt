package com.bijoy.paymentauth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.bijoy.paymentauth.controller.Controller
import com.bijoy.paymentauth.ui.App

@Composable
fun LaunchPaymentSDK(
    onPaymentSuccess: (paymentId: String) -> Unit = {},
    onPaymentError: (code: Int, message: String) -> Unit = { _, _ -> }
) {
    Controller.onPaymentSuccess = onPaymentSuccess
    Controller.onPaymentError = onPaymentError

    val selectedImage by Controller.selectedImage

    App(
        selectedImage = selectedImage,
        onPickImage = { Controller.pickImage() },
        onDoPaymentClick = { Controller.startBiometric() }
    )
}