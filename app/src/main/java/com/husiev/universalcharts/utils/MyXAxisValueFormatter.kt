package com.husiev.universalcharts.utils

import com.github.mikephil.charting.formatter.ValueFormatter

class MyXAxisValueFormatter(values: List<String>): ValueFormatter() {
    private val _values: List<String> = values

    override fun getFormattedValue(value: Float): String {
        return _values[value.toInt() + 1]
    }
}