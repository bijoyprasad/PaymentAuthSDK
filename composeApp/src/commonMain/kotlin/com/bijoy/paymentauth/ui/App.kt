package com.bijoy.paymentauth.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.bijoy.paymentauth.navigation.HomeRoute
import com.bijoy.paymentauth.navigation.SignUpRoute

@Composable
fun App(
    selectedImage: ImageBitmap? = null,
    onPickImage: () -> Unit = {},
    onDoPaymentClick: () -> Unit = {}
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SignUpRoute
    ) {
        composable<SignUpRoute> {
            SignUpScreen(
                selectedImage = selectedImage,
                onPickImage = onPickImage,
                onContinue = { username ->
                    navController.navigate(HomeRoute(username))
                }
            )
        }
        composable<HomeRoute> {
            val route = it.toRoute<HomeRoute>()
            HomeScreen(
                username = route.username,
                onDoPaymentClick = onDoPaymentClick
            )
        }
    }
}
