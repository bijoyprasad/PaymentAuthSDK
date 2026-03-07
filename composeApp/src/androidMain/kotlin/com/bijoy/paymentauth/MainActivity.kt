package com.bijoy.paymentauth

import android.app.AlertDialog
import android.os.Bundle
import android.graphics.Bitmap
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.graphics.asImageBitmap
import android.net.Uri
import android.graphics.BitmapFactory
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import kotlin.let

class MainActivity : ComponentActivity() {

    private val selectedImageState = mutableStateOf<ImageBitmap?>(null)
    private val galleryPicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val inputStream = contentResolver.openInputStream(it)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                selectedImageState.value = bitmap?.asImageBitmap()
            }
        }
    private val cameraPicker =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
            bitmap?.let {
                selectedImageState.value = it.asImageBitmap()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val image by selectedImageState

            App(
                selectedImage = image,
                onPickImage = {
                    val options = arrayOf("Camera", "Gallery")
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Select Profile Image")
                    builder.setItems(options) { _, index ->
                        when (index) {
                            0 -> cameraPicker.launch(null)
                            1 -> galleryPicker.launch("image/*")
                        }
                    }
                    builder.show()
                }
            )
        }
    }
}