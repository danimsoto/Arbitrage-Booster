package com.danmorsot.arbitragebooster.ui.arbitrage

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.math.BigDecimal
import java.math.RoundingMode


var finishedLoading: Boolean = false
var screenArbitrages: MutableMap<String, MutableMap<String, Any>> = mutableMapOf()


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun MyArbitragesScreen(
    navController: NavController, userEmail: String?, viewModel: MyArbitragesViewModel
) {

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = "My orders",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center, color = Color.White
                )
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = Color(251, 150, 0),
                navigationIconContentColor = Color.White
            ),
            navigationIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Arrow Back",
                    modifier = Modifier.clickable {
                        navController.popBackStack()
                    }
                )
            })

    }) {
        viewModel.getDataFromDB()
        if (!finishedLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    modifier = Modifier.size(60.dp),
                    color = Color(251, 150, 0)
                )
            }
        } else {
            MyArbitragesContent(navController, userEmail, screenArbitrages)
        }
    }
}


@Composable
fun BuildDBElement(dbData: MutableMap<String, MutableMap<String, Any>>) {

    for (data in dbData) {
        val arbitrageId = data.key
        val amount = data.value["amount"]
        val buyPrice = data.value["buyPrice"]
        val sellPrice = data.value["sellPrice"]

        amount?.let {
            buyPrice?.let {
                sellPrice?.let {
                    ArbitrageElement(
                        amount = amount.toString(),
                        buyPrice = buyPrice.toString(),
                        sellPrice = sellPrice.toString()
                    )
                }
            }
        }
    }
}


@Composable
fun MyArbitragesContent(
    navController: NavController,
    userEmail: String?,
    dbData: MutableMap<String, MutableMap<String, Any>>
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .padding(top = 70.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        if (dbData.isNotEmpty()) {
            BuildDBElement(dbData)
        }

    }

}


@Composable
fun ArbitrageElement(amount: String, buyPrice: String, sellPrice: String) {

    val amountN = amount.toBigDecimal()
    val buyPriceN = buyPrice.toBigDecimal()
    val sellPriceN = sellPrice.toBigDecimal()
    var profitColor: Color = Color.Green

    val profit = amountN / buyPriceN - amountN / sellPriceN

    if (profit.signum() < 0) {
        profitColor = Color.Red
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(255, 248, 247)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = profit.setScale(2, RoundingMode.UP)
                        .toString() + " €",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 50.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontFamily = FontFamily.SansSerif,
                    color = profitColor
                )
            }
            Column(
                Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Text("Amount: $amount", modifier = Modifier.weight(1f))
                Text("Buy price: $buyPrice", modifier = Modifier.weight(1f))
                Text("Sell price: $sellPrice", modifier = Modifier.weight(1f))
            }
        }
    }
}

