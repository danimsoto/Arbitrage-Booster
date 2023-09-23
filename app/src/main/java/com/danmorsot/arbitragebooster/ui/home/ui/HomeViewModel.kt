package com.danmorsot.arbitragebooster.ui.home.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.danmorsot.arbitragebooster.ui.navigation.AppScreens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore


class HomeViewModel : ViewModel() {

    private val _totalProfit = MutableLiveData<Int>()
    val totalProfit: LiveData<Int> = _totalProfit
    
    fun onSignOutSelected(navController: NavController) {
        navController.navigate(AppScreens.LoginScreen.route)
        FirebaseAuth.getInstance().signOut()
    }

    fun getTotalProfit() {
        val db = FirebaseFirestore.getInstance()
        val userEmail = FirebaseAuth.getInstance().currentUser?.email
        val userOrders =
            userEmail?.let { db.collection("users").document(userEmail).collection("arbitrages") }

        val ordersCount = userOrders?.count()

        ordersCount?.get(AggregateSource.SERVER)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _totalProfit.value = task.result.count.toInt()

            }
            if (task.isComplete) {
                _totalProfit.value = task.result.count.toInt()

            } else {
                task.exception?.message?.let {
                    print(it)
                }
            }
        }
    }

}

