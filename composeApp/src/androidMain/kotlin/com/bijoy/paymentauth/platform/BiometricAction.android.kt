
package com.bijoy.paymentauth.platform

import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.bijoy.paymentauth.ActivityTracker

actual fun biometricAction(
    onSuccess: () -> Unit
) {
    val activity = ActivityTracker.requireCurrentActivity()

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

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Verify your identity")
        .setSubtitle("Authenticate to proceed with payment")
        .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
        .build()

    val executor = ContextCompat.getMainExecutor(activity)
    val biometricPrompt = BiometricPrompt(
        activity,
        executor,
        object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(
                    activity,
                    errString,
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

    biometricPrompt.authenticate(promptInfo)
}