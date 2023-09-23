package com.danmorsot.arbitragebooster.ui.register.ui

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.danmorsot.arbitragebooster.ui.navigation.AppScreens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class RegisterViewModel : ViewModel() {


    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _registerEnable = MutableLiveData<Boolean>()
    val registerEnable: LiveData<Boolean> = _registerEnable

    private val _registerSuccess = MutableLiveData<Boolean>()
    val registerSuccess: LiveData<Boolean> = _registerSuccess


    fun onRegisterChanged(email: String, password: String) {
        _email.value = email
        _password.value = password
        _registerEnable.value = isValidEmail(email) && isValidPassword(password)
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length > 6
    }

    fun onRegisterSelected(navController: NavController, email: String, password: String) {
        val db = FirebaseFirestore.getInstance()

        val personalData: MutableMap<String, Any> = HashMap()
        personalData["name"] = "Name"
        personalData["age"] = "Age"

        _registerEnable.value = false
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    db.collection("users").document(email).set(personalData)
                    _registerSuccess.value = true
                    navController.navigate(AppScreens.LoginScreen.route)

                    _registerSuccess.value = false
                } else {
                    _registerEnable.value = true
                }
            }

    }

    fun onBackLoginSelected(navController: NavController) {
        navController.popBackStack()
    }
}
