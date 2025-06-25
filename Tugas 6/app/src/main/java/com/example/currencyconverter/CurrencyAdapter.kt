package com.example.currencyconverter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class CurrencyAdapter(
    private val context: Context,
    private val currencies: List<String>
) : ArrayAdapter<String>(context, 0, currencies) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    private fun createItemView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false)
        val imageView = view.findViewById<ImageView>(R.id.ivFlag)
        val textView = view.findViewById<TextView>(R.id.tvCurrency)

        val currency = currencies[position].lowercase()
        val resId = context.resources.getIdentifier("flag_$currency", "drawable", context.packageName)
        imageView.setImageResource(resId)
        textView.text = currencies[position]

        return view
    }
}
