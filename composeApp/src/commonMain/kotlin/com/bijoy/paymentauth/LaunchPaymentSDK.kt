package com.bijoy.paymentauth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.bijoy.paymentauth.controller.CommonController
import com.bijoy.paymentauth.ui.App

@Composable
fun LaunchPaymentSDK(
    onPaymentSuccess: (paymentId: String) -> Unit = {},
    onPaymentError: (code: Int, message: String) -> Unit = { _, _ -> }
) {
    CommonController.onPaymentSuccess = onPaymentSuccess
    CommonController.onPaymentError = onPaymentError

    val selectedImage by CommonController.selectedImage

    App(
        selectedImage = selectedImage,
        onPickImage = { CommonController.pickImage() },
        onDoPaymentClick = { CommonController.startBiometric() }
    )
}