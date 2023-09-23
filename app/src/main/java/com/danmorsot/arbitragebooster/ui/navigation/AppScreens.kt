package com.danmorsot.arbitragebooster.ui.navigation

sealed class AppScreens(val route: String) {
    object SplashScren : AppScreens("splash_screen")
    object LoginScreen : AppScreens("login_screen")
    object RegisterScreen : AppScreens("register_screen")
    object HomeScreen : AppScreens("home_screen")
    object ChatScreen : AppScreens("chat_screen")
    object NewArbitrageScreen : AppScreens("new_arbitrage_screen")
    object MyArbitragesScreen : AppScreens("my_arbitrages_screen")
}
