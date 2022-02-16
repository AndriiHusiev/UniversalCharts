package com.husiev.universalcharts.charts

import android.graphics.PointF

data class SimpleChart(val label: String) {
    var data = mutableListOf<PointF>()
    var skipCell = arrayOf<Boolean>()
}
