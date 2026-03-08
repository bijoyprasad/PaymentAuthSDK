package com.bijoy.paymentauth.controller

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import com.bijoy.paymentauth.platform.biometricAction
import com.bijoy.paymentauth.platform.imagePickAction
import com.bijoy.paymentauth.platform.paymentAction

internal object CommonController {

    val selectedImage = mutableStateOf<ImageBitmap?>(null)

    var onPaymentSuccess: ((paymentId: String) -> Unit)? = null
    var onPaymentError: ((code: Int, message: String) -> Unit)? = null

    fun pickImage() {
        imagePickAction {}
    }

    fun startBiometric() {
        biometricAction {
            paymentAction(
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