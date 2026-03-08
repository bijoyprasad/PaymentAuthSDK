// androidMain: com/bijoy/paymentauth/SDKActivityTracker.kt
package com.bijoy.paymentauth

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import java.lang.ref.WeakReference
internal object SDKActivityTracker : Application.ActivityLifecycleCallbacks {

    private var currentActivity: WeakReference<FragmentActivity>? = null

    fun register(app: Application) {
        app.registerActivityLifecycleCallbacks(this)
    }

    fun getCurrentActivity(): FragmentActivity? = currentActivity?.get()

    fun requireCurrentActivity(): FragmentActivity =
        getCurrentActivity()
            ?: throw IllegalStateException(
                "PaymentSDK is not initialized. " +
                        "Call PaymentSDK.init(application) in your Application.onCreate() before using the SDK."
            )

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity is FragmentActivity) {
            currentActivity = WeakReference(activity)
        }
    }

    override fun onActivityStarted(activity: Activity) {
        if (activity is FragmentActivity) {
            currentActivity = WeakReference(activity)
        }
    }

    override fun onActivityResumed(activity: Activity) {
        if (activity is FragmentActivity) {
            currentActivity = WeakReference(activity)
        }
    }

    // ← removed onActivityPaused clearing, BiometricPrompt pauses the activity!

    override fun onActivityPaused(activity: Activity) {} // ← do nothing

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivityDestroyed(activity: Activity) {
        if (currentActivity?.get() == activity) {
            currentActivity = null  // ← only clear when truly gone
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
}