package com.danmorsot.arbitragebooster.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.danmorsot.arbitragebooster.R
import com.danmorsot.arbitragebooster.ui.navigation.AppScreens
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {

    LaunchedEffect(key1 = true) {
        delay(2000)
        navController.popBackStack()
        navController.navigate(AppScreens.LoginScreen.route)
    }
    
    Splash()
}

@Composable
fun Splash() {


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth(),
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = "Main APP logo"
        )

        Spacer(modifier = Modifier.size(150.dp))

        CircularProgressIndicator(modifier = Modifier.size(70.dp), color = Color(251, 150, 0))
    }

}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    Splash()
}