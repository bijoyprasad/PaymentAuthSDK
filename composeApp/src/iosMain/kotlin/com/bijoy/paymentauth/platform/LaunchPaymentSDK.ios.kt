package com.bijoy.paymentauth.platform

import androidx.compose.runtime.getValue
import androidx.compose.ui.window.ComposeUIViewController
import com.bijoy.paymentauth.controller.Controller
import com.bijoy.paymentauth.ui.App
import platform.UIKit.UIApplication
import platform.UIKit.UIModalPresentationFullScreen
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowScene

actual fun launchPaymentSDK(
    onSuccess: (paymentId: String) -> Unit,
    onError: (code: Int, message: String) -> Unit
) {
    val rootViewController = getTopViewController() ?: return

    var sdkViewController: platform.UIKit.UIViewController? = null

    Controller.onPaymentSuccess = { paymentId ->
        onSuccess(paymentId)
        Controller.reset()
        sdkViewController?.dismissViewControllerAnimated(true, completion = null) // ← finish()
    }

    Controller.onPaymentError = { code, message ->
        onError(code, message)
        Controller.reset()
        sdkViewController?.dismissViewControllerAnimated(true, completion = null) // ← finish()
    }

    sdkViewController = ComposeUIViewController {
        val selectedImage by Controller.selectedImage
        App(
            selectedImage = selectedImage,
            onPickImage = { Controller.pickImage() },
            onDoPaymentClick = { Controller.startBiometric() }
        )
    }.also {
        it.modalPresentationStyle = UIModalPresentationFullScreen // ← full screen like Activity
        rootViewController.presentViewController(
            viewControllerToPresent = it,
            animated = true,
            completion = null
        )
    }
}

private fun getTopViewController(): platform.UIKit.UIViewController? {
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
    return top
}