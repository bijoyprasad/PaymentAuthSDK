package com.bijoy.paymentauth.platform

import androidx.compose.ui.graphics.ImageBitmap

expect fun pickImage(
    onResult: (ImageBitmap?) -> Unit
)