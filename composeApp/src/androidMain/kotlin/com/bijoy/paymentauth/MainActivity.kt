package com.bijoy.paymentauth

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity
import com.bijoy.paymentauth.features.RazorpayManager

class MainActivity : FragmentActivity() {

//    private val selectedImageState = mutableStateOf<ImageBitmap?>(null)
//    private val galleryPicker =
//        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
//            uri?.let {
//                val inputStream = contentResolver.openInputStream(it)
//                val bitmap = BitmapFactory.decodeStream(inputStream)
//                selectedImageState.value = bitmap?.asImageBitmap()
//            }
//        }
//    private val cameraPicker =
//        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
//            bitmap?.let {
//                selectedImageState.value = it.asImageBitmap()
//            }
//        }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            PaymentSDKEntry(
                onPaymentSuccess = { paymentId ->
                    Toast.makeText(this, "Payment Success: $paymentId", Toast.LENGTH_LONG).show()
                },
                onPaymentError = { code, message ->
                    Toast.makeText(this, "Payment Failed: $message", Toast.LENGTH_LONG).show()
                }
            )
        }

//        Checkout.preload(applicationContext);
//
//        setContent {
//            val image by selectedImageState
//
//            App(
//                selectedImage = image,
//                onPickImage = {
//                    val options = arrayOf("Camera", "Gallery")
//                    val builder = AlertDialog.Builder(this)
//                    builder.setTitle("Select Profile Image")
//                    builder.setItems(options) { _, index ->
//                        when (index) {
//                            0 -> cameraPicker.launch(null)
//                            1 -> galleryPicker.launch("image/*")
//                        }
//                    }
//                    builder.show()
//                },
//                onDoPaymentClick = {
//                    biometricAuthentication()
//                }
//
//            )
//        }

    }


    private fun biometricAuthentication() {
//        val bio = BiometricAuth()
//        if(bio.isBiometricAvailable(this)) {
//            bio.showBiometricDialog(this)
//        }

        RazorpayManager().startPayment(
            activity = this@MainActivity,
            amount = 500,
            userName = "Arindam",
            email = "test@gmail.com"
        )
    }

//    override fun onPaymentSuccess(razorpayPaymentID: String?) {
//
//        Toast.makeText(
//            this,
//            "Payment Success: $razorpayPaymentID",
//            Toast.LENGTH_LONG
//        ).show()
//    }
//
//    override fun onPaymentError(code: Int, response: String?) {
//
//        Toast.makeText(
//            this,
//            "Payment Failed: $response",
//            Toast.LENGTH_LONG
//        ).show()
//    }
}