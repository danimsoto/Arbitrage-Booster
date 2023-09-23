package com.danmorsot.arbitragebooster.ui.arbitrage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import java.math.RoundingMode

class NewArbitrageViewModel : ViewModel() {

    private val _amount = MutableLiveData<String>()
    val amount: LiveData<String> = _amount

    private val _buyPrice = MutableLiveData<String>()
    val buyPrice: LiveData<String> = _buyPrice

    private val _sellPrice = MutableLiveData<String>()
    val sellPrice: LiveData<String> = _sellPrice

    private val _profit = MutableLiveData<String>()
    val profit: LiveData<String> = _profit

    private val _calculateEnable = MutableLiveData<Boolean>()
    val calculateEnable: LiveData<Boolean> = _calculateEnable

    fun onDataChanged(amount: String, buyPrice: String, sellPrice: String) {
        _amount.value = amount
        _sellPrice.value = sellPrice
        _buyPrice.value = buyPrice
        _calculateEnable.value = isValidData(amount, buyPrice, sellPrice)
    }

    private fun isValidData(amount: String, buyPrice: String, sellPrice: String): Boolean {
        return amount != "" && buyPrice != "" && sellPrice != ""
    }

    fun onCalcSelected() {
        val calc1 = (_amount.value?.toFloat())?.div((_buyPrice.value?.toFloat()!!))
        val calc2 = (_amount.value?.toFloat())?.div((_sellPrice.value?.toFloat()!!))
        val res: Float = calc1!! - calc2!!
        _profit.value = res.toBigDecimal().setScale(2, RoundingMode.UP).toString() + " €"
    }

    fun onAddSelected() {
        val amount = _amount.value?.toFloat()
        val buyPrice = _buyPrice.value?.toFloat()
        val sellPrice = _sellPrice.value?.toFloat()

        val db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser?.email
        val arbitrageData: MutableMap<String, String> = HashMap()

        amount?.let {
            buyPrice?.let {
                sellPrice?.let {
                    currentUser?.let {
                        arbitrageData["amount"] = amount.toString()
                        arbitrageData["buyPrice"] = buyPrice.toString()
                        arbitrageData["sellPrice"] = sellPrice.toString()

                        val userDocument = db.collection("users").document(currentUser)
                        val arbCollection = userDocument.collection("arbitrages")
                        val arbIndexQuery = arbCollection.count()

                        arbIndexQuery.get(AggregateSource.SERVER).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val snapshot = task.result
                                val arbIndex = (snapshot.count + 1).toString()
                                arbCollection.document("Arbitrage $arbIndex").set(arbitrageData)
                                _amount.value = ""
                                _buyPrice.value = ""
                                _sellPrice.value = ""
                            } else if (task.isComplete) {
                                _amount.value = ""
                                _buyPrice.value = ""
                                _sellPrice.value = ""
                            } else {
                                Log.d(
                                    "Error creating arbitrage index",
                                    "Count failed: ",
                                    task.exception
                                )
                            }
                        }
                    }
                }
            }
        }
    }


}

