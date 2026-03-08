package com.bijoy.paymentauth.platform

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import platform.Foundation.NSError
import platform.LocalAuthentication.LAContext
import platform.LocalAuthentication.LAPolicyDeviceOwnerAuthentication
import platform.LocalAuthentication.LAPolicyDeviceOwnerAuthenticationWithBiometrics
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowScene
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

@OptIn(ExperimentalForeignApi::class)
actual fun biometricAction(
    onSuccess: () -> Unit
) {
    val context = LAContext()

    val canEvaluate = memScoped {
        val errorPtr = alloc<kotlinx.cinterop.ObjCObjectVar<NSError?>>()
        context.canEvaluatePolicy(
            policy = LAPolicyDeviceOwnerAuthenticationWithBiometrics,
            error = errorPtr.ptr
        )
    }

    if (!canEvaluate) {
        val canUsePasscode = memScoped {
            val errorPtr = alloc<kotlinx.cinterop.ObjCObjectVar<NSError?>>()
            context.canEvaluatePolicy(
                policy = LAPolicyDeviceOwnerAuthentication,
                error = errorPtr.ptr
            )
        }
        if (canUsePasscode) {
            evaluateBiometric(
                context = context,
                policy = LAPolicyDeviceOwnerAuthentication,
                onSuccess = onSuccess
            )
        } else {
            showAlert(
                title = "Authentication Unavailable",
                message = "Face ID and passcode are not set up on this device. " +
                        "Please enable them in Settings."
            )
        }
        return
    }

    evaluateBiometric(
        context = context,
        policy = LAPolicyDeviceOwnerAuthenticationWithBiometrics,
        onSuccess = onSuccess
    )
}

private fun evaluateBiometric(
    context: LAContext,
    policy: platform.LocalAuthentication.LAPolicy,
    onSuccess: () -> Unit
) {
    context.evaluatePolicy(
        policy = policy,
        localizedReason = "Authenticate to proceed with payment",
    ) { success, error ->
        dispatch_async(dispatch_get_main_queue()) {
            if (success) {
                onSuccess()
            } else {
                val errorMessage = when (error?.code?.toInt()) {
                    -2   -> null
                    -3   -> "Authentication cancelled."
                    -8   -> "Face ID is locked. Please use your passcode."
                    -7   -> "Face ID is not set up. Go to Settings → Face ID & Passcode."
                    -6   -> "Face ID is not available on this device."
                    else -> error?.localizedDescription ?: "Authentication failed. Please try again."
                }
                errorMessage?.let { showAlert(title = "Authentication Failed", message = it) }
            }
        }
    }
}

private fun showAlert(title: String, message: String) {
    val alert = UIAlertController.alertControllerWithTitle(
        title = title,
        message = message,
        preferredStyle = UIAlertControllerStyleAlert
    )
    alert.addAction(
        UIAlertAction.actionWithTitle(title = "OK", style = UIAlertActionStyleDefault) { }
    )

    val windowScene = UIApplication.sharedApplication
        .connectedScenes
        .filterIsInstance<UIWindowScene>()
        .firstOrNull()
    var top = windowScene
        ?.windows
        ?.filterIsInstance<UIWindow>()
        ?.firstOrNull { it.isKeyWindow() }
        ?.rootViewController
    while (top?.presentedViewController != null) {
        top = top.presentedViewController
    }
    top?.presentViewController(viewControllerToPresent = alert, animated = true, completion = null)
}