package com.danmorsot.arbitragebooster.ui.arbitrage

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewArbitrageScreen(
    viewModel: NewArbitrageViewModel,
    navController: NavController,
    userEmail: String?
) {
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = "New order",
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
        NewArbitrageContent(viewModel, navController, userEmail)
    }
}

@Composable
fun NewArbitrageContent(
    viewModel: NewArbitrageViewModel,
    navController: NavController,
    userEmail: String?
) {
    val amount: String by viewModel.amount.observeAsState("")
    val buyPrice: String by viewModel.buyPrice.observeAsState("")
    val sellPrice: String by viewModel.sellPrice.observeAsState("")
    val calculatedProfit: String by viewModel.profit.observeAsState("")
    val calculateEnabled: Boolean by viewModel.calculateEnable.observeAsState(initial = false)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp),

        )
    {

        ArbitrageText()

        Spacer(modifier = Modifier.padding(bottom = 25.dp))

        AmountField(amount) { viewModel.onDataChanged(it, buyPrice, sellPrice) }

        Spacer(modifier = Modifier.padding(bottom = 25.dp))

        BuyPriceField(buyPrice) { viewModel.onDataChanged(amount, it, sellPrice) }

        Spacer(modifier = Modifier.padding(bottom = 25.dp))

        SellPriceField(sellPrice) { viewModel.onDataChanged(amount, buyPrice, it) }

        CalculateButton(viewModel, calculateEnabled)

        Spacer(modifier = Modifier.padding(bottom = 30.dp))

        ProfitCard(calculatedProfit, amount) { viewModel.onAddSelected() }
    }
}

@Composable
fun ArbitrageText() {
    Text(
        modifier = Modifier.padding(start = 25.dp),
        text = "New Arbitrage",
        fontSize = 25.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.SansSerif,
        color = Color(251, 150, 0),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmountField(amount: String, onTextFieldChanged: (String) -> Unit) {
    TextField(
        label = { Text(text = "Amount") },
        value = amount,
        onValueChange = { onTextFieldChanged(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 25.dp, end = 25.dp),
        placeholder = { Text(text = "(€)") },
        singleLine = true,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
fun BuyPriceField(buyPrice: String, onTextFieldChanged: (String) -> Unit) {
    TextField(
        label = { Text(text = "Buy price") },
        value = buyPrice,
        onValueChange = { onTextFieldChanged(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 25.dp, end = 25.dp),
        placeholder = { Text(text = "(€)") },
        singleLine = true,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
fun SellPriceField(sellPrice: String, onTextFieldChanged: (String) -> Unit) {
    TextField(
        label = { Text(text = "Sell price") },
        value = sellPrice,
        onValueChange = { onTextFieldChanged(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 25.dp, end = 25.dp, bottom = 30.dp),
        placeholder = { Text(text = "(€)") },
        singleLine = true,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color(0xFF444446),
            containerColor = Color(0xFFDEDDDD),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )

}

@Composable
fun CalculateButton(viewModel: NewArbitrageViewModel, calculateEnabled: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { viewModel.onCalcSelected() },
            modifier = Modifier
                .width(150.dp)
                .height(35.dp)
                .padding(start = 16.dp, end = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(251, 150, 0),
                disabledContainerColor = Color(255, 227, 145, 255),
                contentColor = Color.White,
                disabledContentColor = Color.White
            ),
            enabled = calculateEnabled

        ) {
            Text(text = "Calculate")
        }
    }
}


@Composable
fun ProfitCard(calculatedProfit: String, amount: String, onAddSelected: () -> Unit) {
    var profitColor = Color(50, 168, 82)
    val context = LocalContext.current
    if (calculatedProfit != "" && amount != "") {
        if (calculatedProfit.dropLast(2).toFloat() < 0) {
            profitColor = Color.Red
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .width(230.dp)
                    .height(180.dp),
                colors = CardDefaults.cardColors(containerColor = Color(255, 248, 247)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        text = "Profit",
                        textAlign = TextAlign.Center,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        color = Color(251, 150, 0)
                    )

                    Text(
                        text = calculatedProfit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        color = profitColor
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                onAddSelected()
                                Toast.makeText(context, "Saved successfully", Toast.LENGTH_LONG)
                                    .show()
                            },
                            modifier = Modifier
                                .width(150.dp)
                                .height(40.dp)
                                .padding(start = 16.dp, end = 16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(251, 150, 0),
                                disabledContainerColor = Color(255, 227, 145, 255),
                                contentColor = Color.White,
                                disabledContentColor = Color.White
                            ),
                        ) {


                            Image(
                                modifier = Modifier
                                    .height(40.dp)
                                    .width(40.dp),
                                painter = painterResource(id = com.danmorsot.arbitragebooster.R.drawable.save_icon),
                                contentDescription = "Save order into database"
                            )
                            Text(text = "Save")
                        }


                    }
                }
            }
        }
    }

}