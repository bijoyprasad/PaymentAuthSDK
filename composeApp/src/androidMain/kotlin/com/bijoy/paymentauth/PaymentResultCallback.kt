
package com.bijoy.paymentauth

internal object PaymentResultCallback {
    var onSuccess: ((paymentId: String) -> Unit)? = null
    var onError: ((code: Int, message: String) -> Unit)? = null

    fun onSuccessCallback(paymentId: String) {
        onSuccess?.invoke(paymentId)
        clear()
    }

    fun onErrorCallback(code: Int, message: String) {
        onError?.invoke(code, message)
        clear()
    }

    private fun clear() {
        onSuccess = null
        onError = null
    }
}