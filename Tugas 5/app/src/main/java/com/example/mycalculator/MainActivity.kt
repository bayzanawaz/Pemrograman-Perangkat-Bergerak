package com.example.mycalculator

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mycalculator.R


class MainActivity : AppCompatActivity() {

    private fun sum(a: Int, b: Int): Int = a + b
    private fun sub(a: Int, b: Int): Int = a - b
    private fun mul(a: Int, b: Int): Int = a * b
    private fun div(a: Int, b: Int): String = if (b != 0) (a / b).toString() else "Cannot divide by zero"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputA = findViewById<EditText>(R.id.inputA)
        val inputB = findViewById<EditText>(R.id.inputB)
        val resultText = findViewById<TextView>(R.id.resultText)

        val addButton = findViewById<Button>(R.id.addButton)
        val subButton = findViewById<Button>(R.id.subButton)
        val mulButton = findViewById<Button>(R.id.mulButton)
        val divButton = findViewById<Button>(R.id.divButton)
        val clearButton = findViewById<Button>(R.id.clearButton)

        fun getInputs(): Pair<Int, Int>? {
            val a = inputA.text.toString()
            val b = inputB.text.toString()
            return if (a.isNotEmpty() && b.isNotEmpty()) {
                Pair(a.toInt(), b.toInt())
            } else {
                Toast.makeText(this, "Please enter both numbers", Toast.LENGTH_SHORT).show()
                null
            }
        }

        addButton.setOnClickListener {
            getInputs()?.let { (a, b) ->
                resultText.text = "Hasil: ${sum(a, b)}"
            }
        }

        subButton.setOnClickListener {
            getInputs()?.let { (a, b) ->
                resultText.text = "Hasil: ${sub(a, b)}"
            }
        }

        mulButton.setOnClickListener {
            getInputs()?.let { (a, b) ->
                resultText.text = "Hasil: ${mul(a, b)}"
            }
        }

        clearButton.setOnClickListener {
            inputA.text.clear()
            inputB.text.clear()
            resultText.text = "Hasil: "
        }

        divButton.setOnClickListener {
            getInputs()?.let { (a, b) ->
                resultText.text = "Hasil: ${div(a, b)}"
            }
        }
    }
}
