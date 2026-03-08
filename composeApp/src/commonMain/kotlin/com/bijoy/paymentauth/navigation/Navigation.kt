package com.bijoy.paymentauth.navigation

import kotlinx.serialization.Serializable

@Serializable
object SignUpRoute

@Serializable
data class HomeRoute(val username: String)