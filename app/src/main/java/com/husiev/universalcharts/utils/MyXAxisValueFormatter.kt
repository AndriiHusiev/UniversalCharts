package com.husiev.universalcharts.utils

import com.github.mikephil.charting.formatter.ValueFormatter

class MyXAxisValueFormatter(values: List<String>): ValueFormatter() {
    private val _values: List<String> = values

    override fun getFormattedValue(value: Float): String {
        val index = value.toInt()
        if (index != _values.lastIndex || index < 0) return ""
        return _values[index]
    }
}