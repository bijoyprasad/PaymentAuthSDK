package com.bijoy.paymentauth

import androidx.compose.runtime.getValue
import androidx.compose.ui.window.ComposeUIViewController
import com.bijoy.paymentauth.controller.Controller
import com.bijoy.paymentauth.manager.imagePickAction
import com.bijoy.paymentauth.ui.App
import platform.UIKit.UIApplication
import platform.UIKit.UIModalPresentationFullScreen
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowScene

actual fun launchPaymentSDK(
    onSuccess: (paymentId: String) -> Unit,
    onError: (code: Int, message: String) -> Unit
) {
    val rootViewController = getTopViewController() ?: return
    var sdkViewController: UIViewController? = null

    // Mirror of LaunchManager.pickImageOverride on Android
    Controller.pickImageOverride = {
        imagePickAction { _ -> } // iOS imagePickAction already sets Controller.selectedImage internally
    }

    Controller.onPaymentSuccess = { paymentId ->
        onSuccess(paymentId)
        Controller.reset()
        sdkViewController?.dismissViewControllerAnimated(true, completion = null)
    }

    Controller.onPaymentError = { code, message ->
        onError(code, message)
        Controller.reset()
        sdkViewController?.dismissViewControllerAnimated(true, completion = null)
    }

    sdkViewController = ComposeUIViewController {
        val selectedImage by Controller.selectedImage
        App(
            selectedImage = selectedImage,
            onPickImage = { Controller.pickImage() },
            onDoPaymentClick = { Controller.startBiometric() }
        )
    }.also {
        it.modalPresentationStyle = UIModalPresentationFullScreen
        rootViewController.presentViewController(
            viewControllerToPresent = it,
            animated = true,
            completion = null
        )
    }
}

private fun getTopViewController(): UIViewController? {
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