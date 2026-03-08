package com.bijoy.paymentauth.controller

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import com.bijoy.paymentauth.platform.authenticate
import com.bijoy.paymentauth.platform.pickImage
import com.bijoy.paymentauth.platform.startPayment

internal object SDKController {

    val selectedImage = mutableStateOf<ImageBitmap?>(null)

    var onPaymentSuccess: ((paymentId: String) -> Unit)? = null
    var onPaymentError: ((code: Int, message: String) -> Unit)? = null

    fun pickImageCall() {
        pickImage { }
    }

    fun startBiometricCall() {
        authenticate {
            startPayment(
                onSuccess = { paymentId ->
                    onPaymentSuccess?.invoke(paymentId)
                },
                onError = { code, message ->
                    onPaymentError?.invoke(code, message)
                }
            )
        }
    }
}