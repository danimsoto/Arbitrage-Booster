package com.danmorsot.arbitragebooster.ui.register.ui

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

import com.danmorsot.arbitragebooster.R


@Composable
fun RegisterScreen(viewModel: RegisterViewModel, navController: NavController) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        RegisterContent(viewModel, navController)
    }
}


@Composable
fun RegisterContent(viewModel: RegisterViewModel, navController: NavController) {
    val email: String by viewModel.email.observeAsState(initial = "")
    val password: String by viewModel.password.observeAsState(initial = "")
    val registerEnabled: Boolean by viewModel.registerEnable.observeAsState(initial = false)
    val registerSuccess: Boolean by viewModel.registerSuccess.observeAsState(initial = false)
    val context = LocalContext.current

    if (registerSuccess) {
        Toast.makeText(context, "Successfully registered, you can now log in", Toast.LENGTH_LONG)
            .show()
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
            RegisterText()
            Spacer(Modifier.size(60.dp))
        }

        item {
            EmailField(email) { viewModel.onRegisterChanged(it, password) }
            Spacer(Modifier.size(25.dp))


        }
        item {
            PasswordField(password) { viewModel.onRegisterChanged(email, it) }
            Spacer(Modifier.size(16.dp))
        }

        item {
            RegisterButton(registerEnabled) {
                viewModel.onRegisterSelected(navController, email, password)
            }
            Spacer(Modifier.size(25.dp))
        }

        item {
            BackLogInText { viewModel.onBackLoginSelected(navController) }
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
fun RegisterText() {
    Text(
        fontSize = 25.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.SansSerif,
        color = Color(251, 150, 0),
        textAlign = TextAlign.Center,
        text = "Register"
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
fun RegisterButton(registerEnabled: Boolean, onRegisterSelected: () -> Unit) {
    Button(
        onClick = { onRegisterSelected() },
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
        enabled = registerEnabled

    ) {
        Text(text = "Register")
    }
}

@Composable
fun BackLogInText(onBackLoginSelected: () -> Unit) {
    Text(
        fontSize = 15.sp,
        fontWeight = FontWeight.ExtraBold,
        fontFamily = FontFamily.SansSerif,
        color = Color(251, 150, 0),
        textAlign = TextAlign.Center,
        text = "Already have an account? Log in!",
        modifier = Modifier
            .clickable { onBackLoginSelected() }
            .fillMaxWidth()
            .padding(end = 16.dp)
    )
}