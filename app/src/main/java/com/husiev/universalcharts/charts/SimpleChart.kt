package com.husiev.universalcharts.charts

import android.graphics.PointF

data class SimpleChart(val label: String) {//, var data: List<PointF>) {
    var data = mutableListOf<PointF>()
    var skipCell = arrayOf<Boolean>()
}
