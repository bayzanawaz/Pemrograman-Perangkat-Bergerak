 package com.example.dessertclicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.dessertclicker.data.Datasource
import com.example.dessertclicker.model.Dessert
import com.example.dessertclicker.ui.theme.DessertClickerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DessertClickerTheme {
                DessertClickerApp()
            }
        }
    }
}

@Composable
fun DessertClickerApp() {
    val desserts = Datasource.dessertList
    var revenue by remember { mutableStateOf(0) }
    var sold by remember { mutableStateOf(0) }

    val currentDessert = remember(revenue, sold) {
        desserts.lastOrNull { sold >= it.startProductionAmount } ?: desserts.first()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(currentDessert.imageId),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .clickable {
                    revenue += currentDessert.price
                    sold++
                }
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text("Desserts Sold: $sold")
        Text("Revenue: $${revenue}")
    }
}
