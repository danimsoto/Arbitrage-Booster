package com.danmorsot.arbitragebooster.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.danmorsot.arbitragebooster.ui.arbitrage.MyArbitragesScreen
import com.danmorsot.arbitragebooster.ui.arbitrage.MyArbitragesViewModel
import com.danmorsot.arbitragebooster.ui.arbitrage.NewArbitrageScreen
import com.danmorsot.arbitragebooster.ui.arbitrage.NewArbitrageViewModel
import com.danmorsot.arbitragebooster.ui.home.chat.ui.ChatScreen
import com.danmorsot.arbitragebooster.ui.home.ui.HomeScreen
import com.danmorsot.arbitragebooster.ui.home.ui.HomeViewModel
import com.danmorsot.arbitragebooster.ui.login.ui.LoginScreen
import com.danmorsot.arbitragebooster.ui.login.ui.LoginViewModel
import com.danmorsot.arbitragebooster.ui.register.ui.RegisterScreen
import com.danmorsot.arbitragebooster.ui.register.ui.RegisterViewModel
import com.danmorsot.arbitragebooster.ui.splash.SplashScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.SplashScren.route) {
        composable(route = AppScreens.SplashScren.route) {
            SplashScreen(navController)
        }

        composable(route = AppScreens.LoginScreen.route) {
            LoginScreen(viewModel = LoginViewModel(), navController)
        }
        composable(
            route = AppScreens.RegisterScreen.route,
        ) {
            RegisterScreen(viewModel = RegisterViewModel(), navController)
        }
        composable(
            route = AppScreens.HomeScreen.route + "/{userEmail}",
            arguments = listOf(navArgument(name = "userEmail") {
                type = NavType.StringType
            })
        ) {
            HomeScreen(navController, it.arguments?.getString("userEmail"), HomeViewModel())
        }
        composable(
            route = AppScreens.ChatScreen.route + "/{userEmail}",
            arguments = listOf(navArgument(name = "userEmail") {
                type = NavType.StringType
            })
        ) {
            ChatScreen(navController, it.arguments?.getString("userEmail"))
        }

        composable(
            route = AppScreens.NewArbitrageScreen.route + "/{userEmail}",
            arguments = listOf(navArgument(name = "userEmail") {
                type = NavType.StringType
            })
        ) {
            NewArbitrageScreen(
                viewModel = NewArbitrageViewModel(),
                navController,
                it.arguments?.getString("userEmail")
            )
        }

        composable(
            route = AppScreens.MyArbitragesScreen.route + "/{userEmail}",
            arguments = listOf(navArgument(name = "userEmail") {
                type = NavType.StringType
            })
        ) {
            MyArbitragesScreen(
                navController,
                it.arguments?.getString("userEmail"),
                MyArbitragesViewModel()
            )
        }
    }


}