package com.husiev.universalcharts.utils

import android.util.Log
import com.github.mikephil.charting.formatter.ValueFormatter

class MyXAxisValueFormatter(values: List<String>): ValueFormatter() {
    private val _values: List<String> = values

    override fun getFormattedValue(value: Float): String {
        Log.d("debug", "$value")
        return _values[value.toInt()]
    }
}