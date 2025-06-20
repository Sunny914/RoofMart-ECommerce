package com.sunny.roofmart

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.sunny.roofmart.pages.CategoryProductsPage
import com.sunny.roofmart.components.ProductDetailsPage
import com.sunny.roofmart.pages.CheckoutPage
import com.sunny.roofmart.screen.AuthScreen
import com.sunny.roofmart.screen.HomeScreen
import com.sunny.roofmart.screen.LoginScreen
import com.sunny.roofmart.screen.SignupScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier){

    val navController = rememberNavController()
    GlobalNavigation.navController = navController

    val isLoggedIn = Firebase.auth.currentUser != null
    val firstPage = if(isLoggedIn) "home" else "auth"

    NavHost(navController = navController, startDestination = firstPage){

        composable("auth"){
            AuthScreen(modifier, navController)
        }

        composable("login"){
            LoginScreen(modifier, navController)
        }

        composable("signup"){
            SignupScreen(modifier, navController)
        }

        composable("home"){
            HomeScreen(modifier, navController)
        }

        composable("category-products/{categoryId}"){
            var categoryId = it.arguments?.getString("categoryId")
            CategoryProductsPage(modifier, categoryId?:"")
        }

        composable("product-details/{productId}"){
            var productId = it.arguments?.getString("productId")
            ProductDetailsPage(modifier, productId?:"")
        }

        composable("checkout"){
            CheckoutPage(modifier)
        }


    }
}

object GlobalNavigation{
    lateinit var navController : NavHostController
}