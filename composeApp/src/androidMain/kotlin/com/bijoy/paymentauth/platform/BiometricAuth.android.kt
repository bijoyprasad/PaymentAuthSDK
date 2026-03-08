// androidMain: com/bijoy/paymentauth/platform/BiometricAuth.android.kt
package com.bijoy.paymentauth.platform

import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.bijoy.paymentauth.SDKActivityTracker

actual fun authenticate(onSuccess: () -> Unit) {
    val activity = SDKActivityTracker.requireCurrentActivity()

    // 1. Check if biometric is available
    val biometricManager = BiometricManager.from(activity)
    val canAuthenticate = biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)

    if (canAuthenticate != BiometricManager.BIOMETRIC_SUCCESS) {
        Toast.makeText(
            activity,
            "Biometric authentication not available on this device",
            Toast.LENGTH_SHORT
        ).show()
        return
    }

    // 2. Build the prompt info
    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Verify your identity")
        .setSubtitle("Authenticate to proceed with payment")
        .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
        .build()

    // 3. Set up callbacks
    val executor = ContextCompat.getMainExecutor(activity)
    val biometricPrompt = BiometricPrompt(
        activity,
        executor,
        object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()   // ← triggers payment flow
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(
                    activity,
                    "Authentication error: $errString",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(
                    activity,
                    "Authentication failed. Please try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    )

    // 4. Show the prompt
    biometricPrompt.authenticate(promptInfo)
}