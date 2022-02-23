package com.husiev.universalcharts.utils

import com.github.mikephil.charting.formatter.ValueFormatter

class MyXAxisValueFormatter(values: List<String>): ValueFormatter() {
    private val _values: List<String> = values

    override fun getFormattedValue(value: Float): String {
        if (value > _values.lastIndex) return ""
        return _values[value.toInt()]
    }
}