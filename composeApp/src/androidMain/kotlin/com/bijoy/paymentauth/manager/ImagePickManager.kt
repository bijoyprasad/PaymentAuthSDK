package com.bijoy.paymentauth.manager

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
import com.bijoy.paymentauth.config.PaymentInitializer
import com.bijoy.paymentauth.controller.Controller
import com.bijoy.paymentauth.ui.App

internal class ImagePickManager : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val galleryPicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                val stream = contentResolver.openInputStream(it)
                val bitmap = BitmapFactory.decodeStream(stream)
                Controller.selectedImage.value = bitmap?.asImageBitmap()
            }
        }

        val cameraPicker = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            bitmap?.let {
                Controller.selectedImage.value = it.asImageBitmap()
            }
        }

        Controller.pickImageOverride = {
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

        Controller.onPaymentSuccess = { paymentId ->
            PaymentInitializer.onPaymentSuccess?.invoke(paymentId)
            Controller.reset()
            finish()
        }

        Controller.onPaymentError = { code, message ->
            PaymentInitializer.onPaymentError?.invoke(code, message)
            Controller.reset()
            finish()
        }

        setContent {
            val selectedImage by Controller.selectedImage
            App(
                selectedImage = selectedImage,
                onPickImage = { Controller.pickImage() },
                onDoPaymentClick = { Controller.startBiometric() }
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Controller.reset()
    }

    companion object Companion {
        fun newIntent(context: Context) = Intent(context, ImagePickManager::class.java)
    }
}