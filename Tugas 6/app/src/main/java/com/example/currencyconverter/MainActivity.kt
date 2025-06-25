package com.example.currencyconverter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val rates = mapOf(
        "IDR" to 1.0,
        "USD" to 16884.99,
        "EUR" to 19150.92,
        "GBP" to 22407.16,
        "INR" to 197.23,
        "CAD" to 12173.58,
        "AUD" to 10736.00,
        "SGD" to 12841.77,
        "MYR" to 3847.84,
        "SAR" to 4500.21,
        "THB" to 503.60,
        "JPY" to 118.25
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etInput = findViewById<EditText>(R.id.etInput)
        val spinnerFrom = findViewById<Spinner>(R.id.spinnerFrom)
        val spinnerTo = findViewById<Spinner>(R.id.spinnerTo)
        val btnSwap = findViewById<Button>(R.id.btnSwap)
        val tvResult = findViewById<TextView>(R.id.tvResult)

        // Setup adapter for spinner
        val currencies = rates.keys.toList()
        val customAdapter = CurrencyAdapter(this, currencies)
        spinnerFrom.adapter = customAdapter
        spinnerTo.adapter = customAdapter

        spinnerFrom.setSelection(currencies.indexOf("IDR"))
        spinnerTo.setSelection(currencies.indexOf("USD"))

        fun convert() {
            val input = etInput.text.toString().toDoubleOrNull()
            if (input == null) {
                tvResult.text = "0"
                return
            }

            val from = spinnerFrom.selectedItem.toString()
            val to = spinnerTo.selectedItem.toString()

            val result = input * (rates[from]!! / rates[to]!!)

            // Format hasil dengan koma (ribuan) dan 2 desimal
            val formatter = NumberFormat.getNumberInstance(Locale.US)
            formatter.maximumFractionDigits = 2
            formatter.minimumFractionDigits = 2

            tvResult.text = "${formatter.format(result)} $to"
        }

        etInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = convert()
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) = convert()
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerFrom.onItemSelectedListener = listener
        spinnerTo.onItemSelectedListener = listener

        btnSwap.setOnClickListener {
            val fromPos = spinnerFrom.selectedItemPosition
            val toPos = spinnerTo.selectedItemPosition
            spinnerFrom.setSelection(toPos)
            spinnerTo.setSelection(fromPos)
        }
    }
}
