package com.bijoy.paymentauth.platform

import androidx.compose.ui.graphics.ImageBitmap

expect fun imagePickAction(
    onResult: (ImageBitmap?) -> Unit
)