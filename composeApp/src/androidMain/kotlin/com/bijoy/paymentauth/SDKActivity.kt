package com.bijoy.paymentauth

import android.app.AlertDialog
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.graphics.asImageBitmap
import androidx.fragment.app.FragmentActivity
import com.bijoy.paymentauth.controller.SDKController
import com.razorpay.PaymentResultListener

internal class SDKActivity : FragmentActivity(), PaymentResultListener {

    private val galleryPicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val stream = contentResolver.openInputStream(it)
                val bitmap = BitmapFactory.decodeStream(stream)
                SDKController.selectedImage.value = bitmap?.asImageBitmap()
            }
            finish()
        }

    private val cameraPicker =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            bitmap?.let {
                SDKController.selectedImage.value = it.asImageBitmap()
            }
            finish()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val options = arrayOf("Camera", "Gallery")
        AlertDialog.Builder(this)
            .setTitle("Select Profile Image")
            .setItems(options) { _, index ->
                when (index) {
                    0 -> cameraPicker.launch(null)
                    1 -> galleryPicker.launch("image/*")
                }
            }
            .setOnCancelListener { finish() }
            .show()
    }

    override fun onPaymentSuccess(razorpayPaymentID: String?) {
        RazorpayResultDispatcher.deliverSuccess(razorpayPaymentID ?: "")
    }

    override fun onPaymentError(code: Int, response: String?) {
        RazorpayResultDispatcher.deliverError(code, response ?: "Payment failed")
    }
}