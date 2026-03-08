package com.bijoy.paymentauth.platform

import android.content.Intent
import androidx.compose.ui.graphics.ImageBitmap
import com.bijoy.paymentauth.SDKActivity
import com.bijoy.paymentauth.SDKActivityTracker

actual fun pickImage(onResult: (ImageBitmap?) -> Unit) {
    val activity = SDKActivityTracker.requireCurrentActivity()

    //val context = SDKApplicationContext.get()
    val intent = Intent(activity, SDKActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    activity.startActivity(intent)

}