package com.bijoy.paymentauth.platform

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.bijoy.paymentauth.controller.CommonController
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import org.jetbrains.skia.Image
import platform.Foundation.NSData
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleCancel
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleActionSheet
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerEditedImage
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowScene
import platform.darwin.NSObject

actual fun imagePickAction(
    onResult: (ImageBitmap?) -> Unit
) {
    val rootViewController = getTopViewController() ?: return

    val actionSheet = UIAlertController.alertControllerWithTitle(
        title = "Select Profile Image",
        message = null,
        preferredStyle = UIAlertControllerStyleActionSheet
    )

    if (UIImagePickerController.isSourceTypeAvailable(
            UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
        )
    ) {
        actionSheet.addAction(UIAlertAction.actionWithTitle(
            title = "Camera",
            style = UIAlertActionStyleDefault
        ) {
            presentPicker(
                sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera,
                onResult = onResult
            )
        })
    }

    actionSheet.addAction(UIAlertAction.actionWithTitle(
        title = "Gallery",
        style = UIAlertActionStyleDefault
    ) {
        presentPicker(
            sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary,
            onResult = onResult
        )
    })

    actionSheet.addAction(UIAlertAction.actionWithTitle(
        title = "Cancel",
        style = UIAlertActionStyleCancel
    ) { })

    rootViewController.presentViewController(
        viewControllerToPresent = actionSheet,
        animated = true,
        completion = null
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun presentPicker(
    sourceType: UIImagePickerControllerSourceType,
    onResult: (ImageBitmap?) -> Unit
) {
    val rootViewController = getTopViewController() ?: return

    val picker = UIImagePickerController()
    picker.sourceType = sourceType
    picker.allowsEditing = false

    val delegate = object : NSObject(),
        UIImagePickerControllerDelegateProtocol,
        UINavigationControllerDelegateProtocol {

        override fun imagePickerController(
            picker: UIImagePickerController,
            didFinishPickingMediaWithInfo: Map<Any?, *>
        ) {
            picker.dismissViewControllerAnimated(true, completion = null)
            val uiImage = (didFinishPickingMediaWithInfo[UIImagePickerControllerEditedImage]
                ?: didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage]) as? UIImage
            val imageBitmap = uiImage?.toImageBitmap()
            CommonController.selectedImage.value = imageBitmap
            onResult(imageBitmap)
        }

        override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
            picker.dismissViewControllerAnimated(true, completion = null)
            onResult(null)
        }
    }
    picker.delegate = delegate

    rootViewController.presentViewController(
        viewControllerToPresent = picker,
        animated = true,
        completion = null
    )
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

@OptIn(ExperimentalForeignApi::class)
private fun UIImage.toImageBitmap(): ImageBitmap? {
    val jpegData: NSData = UIImageJPEGRepresentation(this, 0.9) ?: return null
    val bytes = ByteArray(jpegData.length.toInt())
    bytes.usePinned { pinned ->
        platform.posix.memcpy(pinned.addressOf(0), jpegData.bytes, jpegData.length)
    }
    return Image.makeFromEncoded(bytes).toComposeImageBitmap()
}