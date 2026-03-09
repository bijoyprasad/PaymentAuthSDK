package com.bijoy.paymentauth.platform

import android.app.AlertDialog
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.bijoy.paymentauth.config.ActivityTracker
import com.bijoy.paymentauth.controller.Controller

actual fun imagePickAction(
    onResult: (ImageBitmap?) -> Unit
) {
    val context = ActivityTracker.requireCurrentActivity()

    val galleryPicker = context.activityResultRegistry.register(
        "gallery_picker",
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val stream = context.contentResolver.openInputStream(it)
            val bitmap = BitmapFactory.decodeStream(stream)
            Controller.selectedImage.value = bitmap?.asImageBitmap()
        }
    }

    val cameraPicker = context.activityResultRegistry.register(
        "camera_picker",
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            Controller.selectedImage.value = it.asImageBitmap()
        }
    }

    AlertDialog.Builder(context)
        .setTitle("Select Profile Image")
        .setItems(arrayOf("Camera", "Gallery")) { _, index ->
            when (index) {
                0 -> cameraPicker.launch(null)
                1 -> galleryPicker.launch("image/*")
            }
        }
        .setOnCancelListener { }
        .show()
}