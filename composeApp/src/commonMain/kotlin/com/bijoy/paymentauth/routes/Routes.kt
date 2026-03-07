package com.bijoy.paymentauth.routes

import kotlinx.serialization.Serializable

@Serializable
object SignUpRoute

@Serializable
data class HomeRoute(val username: String)