package com.bijoy.paymentauth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import com.bijoy.paymentauth.controller.CommonController
import com.bijoy.paymentauth.ui.App

internal class LaunchManager : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        CommonController.onPaymentSuccess = { paymentId ->
            PaymentSDK.onPaymentSuccess?.invoke(paymentId)
            finish()
        }
        CommonController.onPaymentError = { code, message ->
            PaymentSDK.onPaymentError?.invoke(code, message)
            finish()
        }

        setContent {
            val selectedImage by CommonController.selectedImage
            App(
                selectedImage = selectedImage,
                onPickImage = { CommonController.pickImage() },
                onDoPaymentClick = { CommonController.startBiometric() }
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        CommonController.onPaymentSuccess = null
        CommonController.onPaymentError = null
    }

    companion object Companion {
        fun newIntent(context: Context) = Intent(context, LaunchManager::class.java)
    }
}