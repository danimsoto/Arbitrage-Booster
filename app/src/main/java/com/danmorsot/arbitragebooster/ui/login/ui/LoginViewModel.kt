package com.danmorsot.arbitragebooster.ui.login.ui

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.danmorsot.arbitragebooster.ui.navigation.AppScreens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class LoginViewModel : ViewModel() {


    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _loginEnable = MutableLiveData<Boolean>()
    val loginEnable: LiveData<Boolean> = _loginEnable

    private val _loginError = MutableLiveData<Boolean>()
    val loginError: LiveData<Boolean> = _loginError

    private val _loginClicked = MutableLiveData<Boolean>()
    val loginClicked: LiveData<Boolean> = _loginClicked


    fun onLoginChanged(email: String, password: String) {
        _email.value = email
        _password.value = password
        _loginEnable.value = isValidEmail(email) && isValidPassword(password)
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length > 6
    }

    fun userAlreadyLoggedIn(navController: NavController, userEmail: String) {
        navController.navigate(AppScreens.HomeScreen.route + "/$userEmail")
    }

    fun onLoginSelected(navController: NavController, userEmail: String, password: String) {
        _loginClicked.value = true
        _loginEnable.value = false
        FirebaseAuth.getInstance().signInWithEmailAndPassword(userEmail, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    downloadUserDataFromDB()
                    navController.navigate(AppScreens.HomeScreen.route + "/$userEmail")
                } else {
                    _loginError.value = true
                    _loginEnable.value = true
                    _loginError.value = false
                }
                _loginClicked.value = false
            }

    }

    fun onRegisterSelected(navController: NavController) {
        navController.navigate(AppScreens.RegisterScreen.route)
    }


    private fun downloadUserDataFromDB() {
        val dbLoginData: MutableMap<String, MutableMap<String, Any>> = HashMap()
        val db = FirebaseFirestore.getInstance()
        email.value?.let {
            db.collection("users").document(email.value!!).collection("arbitrages").get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        dbLoginData[document.id] =
                            document.data
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("Error getting user documents", "get failed with ", exception)
                }
        }
        Log.d("Downloaded user data: ", dbLoginData.toString())
    }
}