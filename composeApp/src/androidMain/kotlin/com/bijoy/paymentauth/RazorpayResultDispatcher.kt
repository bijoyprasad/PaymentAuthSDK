// androidMain: com/bijoy/paymentauth/RazorpayResultDispatcher.kt
package com.bijoy.paymentauth

internal object RazorpayResultDispatcher {
    var onSuccess: ((paymentId: String) -> Unit)? = null
    var onError: ((code: Int, message: String) -> Unit)? = null

    fun deliverSuccess(paymentId: String) {
        onSuccess?.invoke(paymentId)
        clear()
    }

    fun deliverError(code: Int, message: String) {
        onError?.invoke(code, message)
        clear()
    }

    private fun clear() {
        onSuccess = null
        onError = null
    }
}