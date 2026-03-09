package com.bijoy.paymentauth

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.fragment.app.FragmentActivity
import com.bijoy.paymentauth.controller.CommonController
import com.bijoy.paymentauth.ui.App

internal class LaunchManager : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val galleryPicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                val stream = contentResolver.openInputStream(it)
                val bitmap = BitmapFactory.decodeStream(stream)
                CommonController.selectedImage.value = bitmap?.asImageBitmap()
            }
        }

        val cameraPicker = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            bitmap?.let {
                CommonController.selectedImage.value = it.asImageBitmap()
            }
        }

        CommonController.pickImageOverride = {
            AlertDialog.Builder(this)
                .setTitle("Select Profile Image")
                .setItems(arrayOf("Camera", "Gallery")) { _, index ->
                    when (index) {
                        0 -> cameraPicker.launch(null)
                        1 -> galleryPicker.launch("image/*")
                    }
                }
                .show()
        }

        CommonController.onPaymentSuccess = { paymentId ->
            PaymentSDK.onPaymentSuccess?.invoke(paymentId)
            CommonController.reset()
            finish()
        }

        CommonController.onPaymentError = { code, message ->
            PaymentSDK.onPaymentError?.invoke(code, message)
            CommonController.reset()
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
        CommonController.reset()
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, LaunchManager::class.java)
    }
}