 package com.example.diceroller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.diceroller.ui.theme.DiceRollerTheme
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.Image
import androidx.compose.material3.Button
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DiceRollerTheme {
            DiceRollerApp()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DiceRollerApp(){
    DiceRollerWithImageAndButton(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(align = Alignment.Center)
    )
}

@Composable
fun DiceRollerWithImageAndButton(modifier: Modifier = Modifier) {
    var result by remember {
        mutableStateOf(1)}
    val imageOfDice = when(result){
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        else -> R.drawable.dice_6
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
        ,modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.Center)) {
        Image(painter = painterResource(id = imageOfDice), contentDescription = null)
        Button(onClick= { result = Random.nextInt(1, 7) }) {
            Text(text = "Roll")
        }
    }
}