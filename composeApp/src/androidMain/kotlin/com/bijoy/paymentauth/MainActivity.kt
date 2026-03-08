package com.bijoy.paymentauth

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            LaunchPaymentSDK(
                onPaymentSuccess = { paymentId ->
                    Toast.makeText(this, "Payment Success: $paymentId", Toast.LENGTH_LONG).show()
                },
                onPaymentError = { code, message ->
                    Log.e( "MainActivity: ", "Error")
                    Toast.makeText(this, "Payment Failed: $message", Toast.LENGTH_LONG).show()
                }
            )
        }
    }
}