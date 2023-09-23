package com.danmorsot.arbitragebooster.ui.arbitrage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.api.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.math.RoundingMode

class MyArbitragesViewModel : ViewModel() {

    private var arbitrages: MutableMap<String, MutableMap<String, Any>> = mutableMapOf()
    fun getDataFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            getDocuments()
        }

    }

    private suspend fun getDocuments() {
        val db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser?.email

        currentUser?.let {
            db.collection("users").document(currentUser).collection("arbitrages").get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        arbitrages[document.id] = document.data
                    }

                }
                .addOnFailureListener { exception ->
                    Log.d("Error getting user documents", "get failed with ", exception)
                }
                .addOnCompleteListener {
                    Log.d("Complete listener dbdata", arbitrages.toString())
                    screenArbitrages = arbitrages
                    finishedLoading = true
                }.await()


        }

    }
}

