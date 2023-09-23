package com.danmorsot.arbitragebooster.ui.login.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.danmorsot.arbitragebooster.R
import com.google.firebase.auth.FirebaseAuth


@Composable
fun LoginScreen(viewModel: LoginViewModel, navController: NavController) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Login(viewModel, navController)
    }
}

@Composable
fun Login(viewModel: LoginViewModel, navController: NavController) {
    val loginClicked: Boolean by viewModel.loginClicked.observeAsState(initial = false)
    LoginContent(viewModel, navController)

    val user = FirebaseAuth.getInstance().currentUser
    if (user != null) {
        viewModel.userAlreadyLoggedIn(navController, user.email!!)
    }
    if (loginClicked) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(modifier = Modifier.size(60.dp), color = Color(251, 150, 0))
        }
    }
}

@Composable
fun LoginContent(viewModel: LoginViewModel, navController: NavController) {

    val email: String by viewModel.email.observeAsState(initial = "")
    val password: String by viewModel.password.observeAsState(initial = "")
    val loginEnabled: Boolean by viewModel.loginEnable.observeAsState(initial = false)
    val loginError: Boolean by viewModel.loginError.observeAsState(initial = false)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current


    if (loginError) {
        Toast.makeText(context, "Login error, please try again", Toast.LENGTH_LONG).show()
    }

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {
            MainLogo()
            Spacer(Modifier.size(60.dp))
        }

        item {
            LoginText()
            Spacer(Modifier.size(60.dp))
        }

        item {
            EmailField(email) { viewModel.onLoginChanged(it, password) }
            Spacer(Modifier.size(25.dp))


        }
        item {
            PasswordField(password) { viewModel.onLoginChanged(email, it) }
            Spacer(Modifier.size(16.dp))
        }

        item {
            ForgotPassword()
            Spacer(Modifier.size(40.dp))
        }

        item {
            LoginButton(loginEnabled) {
                coroutineScope.launch {
                    viewModel.onLoginSelected(navController, email, password)
                }
            }
            Spacer(Modifier.size(25.dp))
        }

        item {
            SignUpText { viewModel.onRegisterSelected(navController) }
        }

    }

}

@Composable
fun MainLogo() {
    Image(
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth(),
        painter = painterResource(id = R.drawable.app_logo),
        contentDescription = "Main APP logo"
    )
}

@Composable
fun LoginText() {
    Text(
        fontSize = 25.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.SansSerif,
        color = Color(251, 150, 0),
        textAlign = TextAlign.Center,
        text = "Log in"
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailField(email: String, onTextFieldChanged: (String) -> Unit) {
    TextField(
        value = email,
        onValueChange = { onTextFieldChanged(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        placeholder = { Text(text = "E-mail address *") },
        singleLine = true,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color(0xFF444446),
            containerColor = Color(0xFFDEDDDD),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordField(password: String, onTextFieldChanged: (String) -> Unit) {
    TextField(
        value = password,
        onValueChange = { onTextFieldChanged(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        placeholder = { Text(text = "Password *") },
        singleLine = true,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color(0xFF444446),
            containerColor = Color(0xFFDEDDDD),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        visualTransformation = PasswordVisualTransformation()
    )
}

@Composable
fun ForgotPassword() {
    val context = LocalContext.current
    Text(
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        fontFamily = FontFamily.SansSerif,
        color = Color(251, 150, 0),
        textAlign = TextAlign.End,
        text = "Forgot password?",
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp)
            .clickable {
                Toast
                    .makeText(context, "Forgot password under construction", Toast.LENGTH_SHORT)
                    .show()
            }
    )
}

@Composable
fun LoginButton(loginEnabled: Boolean, onLoginSelected: () -> Unit) {
    Button(
        onClick = { onLoginSelected() },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(start = 16.dp, end = 16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(251, 150, 0),
            disabledContainerColor = Color(255, 227, 145, 255),
            contentColor = Color.White,
            disabledContentColor = Color.White
        ),
        enabled = loginEnabled

    ) {
        Text(text = "Sign in")
    }
}

@Composable
fun SignUpText(onRegisterSelected: () -> Unit) {
    Text(
        fontSize = 15.sp,
        fontWeight = FontWeight.ExtraBold,
        fontFamily = FontFamily.SansSerif,
        color = Color(251, 150, 0),
        textAlign = TextAlign.Center,
        text = "Don't have an account? Sign up!",
        modifier = Modifier
            .clickable { onRegisterSelected() }
            .fillMaxWidth()
            .padding(end = 16.dp)
    )
}

