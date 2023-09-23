package com.danmorsot.arbitragebooster.ui.home.ui

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.danmorsot.arbitragebooster.models.ChartData
import com.danmorsot.arbitragebooster.ui.navigation.AppScreens
import com.danmorsot.arbitragebooster.R
import com.danmorsot.arbitragebooster.ui.arbitrage.NewArbitrageViewModel
import com.danmorsot.arbitragebooster.ui.arbitrage.MyArbitragesViewModel
import com.danmorsot.arbitragebooster.ui.login.ui.SignUpText
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, userEmail: String?, viewModel: HomeViewModel) {
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = "Home",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center, color = Color.White
                )
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = Color(251, 150, 0)
            )
        )
    }) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(Modifier.size(10.dp))
                TopNavAppBar(
                    navController = navController,
                    userEmail = userEmail,
                )
                Spacer(Modifier.size(25.dp))
            }
            item {
                HomeGraph()
                Spacer(Modifier.size(25.dp))
            }


            item {
                Divider(color = Color.LightGray, thickness = 1.dp)
                viewModel.getTotalProfit()
                StatsData(userEmail, viewModel)
                Divider(color = Color.LightGray, thickness = 1.dp)
            }

            item {
                MainContent(navController = navController, userEmail = userEmail, HomeViewModel())
            }
        }
    }
}

@Composable
fun TopNavAppBar(navController: NavController, userEmail: String?) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 0.dp, end = 8.dp, top = 80.dp, bottom = 30.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        AnalyticsButton()
        NewOrderButton(navController, userEmail)
        OrdersButton(navController, userEmail, MyArbitragesViewModel())
        NotesButton()
    }
}

@Composable
fun AnalyticsButton() {
    Column(
        modifier = Modifier
            .padding(end = 45.dp)
            .clickable(onClick = {}),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier
                .height(50.dp)
                .width(50.dp),
            painter = painterResource(id = R.drawable.analytics_icon),
            contentDescription = "Analytics icon"
        )
        Text(
            "Analytics",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 10.dp),
            fontSize = 12.sp,
            color = Color(0xFF757575)
        )
    }
}

@Composable
fun NewOrderButton(navController: NavController, userEmail: String?) {
    Column(
        modifier = Modifier
            .padding(end = 45.dp)
            .clickable(onClick = { navController.navigate(AppScreens.NewArbitrageScreen.route + "/$userEmail") }),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .height(50.dp)
                .width(50.dp),
            painter = painterResource(id = R.drawable.new_order_icon),
            contentDescription = "New order icon"
        )
        Text(
            "New order",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 10.dp),
            fontSize = 12.sp,
            color = Color(0xFF757575)
        )
    }

}

@Composable
fun OrdersButton(
    navController: NavController,
    userEmail: String?,
    viewModel: MyArbitragesViewModel
) {
    Column(
        modifier = Modifier
            .padding(end = 45.dp)
            .clickable(onClick = {
                navController.navigate(AppScreens.MyArbitragesScreen.route + "/$userEmail")

            }),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .height(50.dp)
                .width(50.dp),
            painter = painterResource(id = R.drawable.orders_icon),
            contentDescription = "Orders icon"
        )
        Text(
            "Orders",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 10.dp),
            fontSize = 12.sp,
            color = Color(0xFF757575)
        )
    }
}

@Composable
fun NotesButton() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            modifier = Modifier
                .height(50.dp)
                .width(50.dp),
            painter = painterResource(id = R.drawable.notes_icon),
            contentDescription = "Notes icon"
        )
        Text(
            "Notes",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 10.dp),
            fontSize = 12.sp,
            color = Color(0xFF757575)
        )
    }
}

@Composable
fun HomeGraphTopText() {
    Row(
        Modifier
            .fillMaxWidth()
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Overview",
            fontSize = 25.sp,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(start = 15.dp),
            textAlign = TextAlign.Left
        )
        Text(
            text = "Ene 01 - Mar 13",
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(end = 15.dp),
            textAlign = TextAlign.Right,
            color = Color(0xFF757575)
        )
    }
}

@Composable
fun HomeGraph() {
    HomeGraphTopText()
    Image(
        modifier = Modifier
            .height(200.dp)
            .width(200.dp),
        painter = painterResource(id = R.drawable.home_graph_image),
        contentDescription = "Graph placeholder"
    )

}

/*
@Composable
fun HomeGraph() {
    HomeGraphTopText()
    val homeLinesData: List<ChartData> =
        listOf(
            ChartData("Ene", 9f),
            ChartData("Feb", 8f),
            ChartData("Mar", 12f)
        )

    val homeLinesPoints = ArrayList<LineChartData.Point>()
    homeLinesData.mapIndexed { _, data ->
        homeLinesPoints.add(
            LineChartData.Point(
                value = data.value,
                label = data.label
            )
        )
    }

    val lines = ArrayList<LineChartData>()
    lines.add(LineChartData(points = homeLinesPoints, lineDrawer = SolidLineDrawer()))

    LineChart(
        linesChartData = lines,
        modifier = Modifier
            .padding(top = 10.dp, bottom = 40.dp)
            .height(150.dp)
            .width(350.dp)
    )
}
*/

@Composable
fun StatsData(userEmail: String?, viewModel: HomeViewModel) {
    val db = FirebaseFirestore.getInstance()
    val userOrders =
        userEmail?.let { db.collection("users").document(userEmail).collection("arbitrages") }

    var numberOfOrders by remember { mutableIntStateOf(0) }

    val ordersCount = userOrders?.count()

    ordersCount?.get(AggregateSource.SERVER)?.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            numberOfOrders = task.result.count.toInt()
        } else {
            task.exception?.message?.let {
                print(it)
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "PERCENTAGE",
                textAlign = TextAlign.Center,
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                text = "3.41%",
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                color = Color(251, 150, 0)
            )


        }
        Column(Modifier.weight(1f)) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "PROFIT",
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),

                text = viewModel.totalProfit.value.toString(),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                color = Color(251, 150, 0)
            )


        }
        Column(Modifier.weight(1f)) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "ORDERS",
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                text = numberOfOrders.toString(),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                color = Color(251, 150, 0)
            )


        }
    }


}


@Composable
fun MainContent(navController: NavController, userEmail: String?, viewModel: HomeViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Spacer(Modifier.size(25.dp))

        userEmail?.let {
            Text(
                modifier = Modifier.padding(start = 25.dp, bottom = 10.dp),
                text = userEmail,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                color = Color(102, 100, 98),
            )
        }

        Spacer(Modifier.size(5.dp))

        SignOutText { viewModel.onSignOutSelected(navController) }


    }
}

@Composable
fun SignOutText(onSignOutSelected: () -> Unit) {
    Text(
        fontSize = 15.sp,
        fontWeight = FontWeight.ExtraBold,
        fontFamily = FontFamily.SansSerif,
        color = Color(251, 150, 0),
        textAlign = TextAlign.Center,
        text = "Logout",
        modifier = Modifier
            .clickable { onSignOutSelected() }
    )
}
