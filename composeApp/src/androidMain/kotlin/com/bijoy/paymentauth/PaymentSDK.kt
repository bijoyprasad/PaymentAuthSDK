// androidMain: com/bijoy/paymentauth/PaymentSDK.kt
package com.bijoy.paymentauth

import android.app.Application

object PaymentSDK {
    fun init(app: Application) {
        //SDKApplicationContext.init(app)
        SDKActivityTracker.register(app)  // ← add this
    }
}