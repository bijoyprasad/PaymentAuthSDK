package com.bijoy.paymentauth

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform