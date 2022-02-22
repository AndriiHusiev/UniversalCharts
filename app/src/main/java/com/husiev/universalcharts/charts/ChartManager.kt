package com.husiev.universalcharts.charts

import android.graphics.PointF
import android.util.Log
import com.husiev.universalcharts.utils.CHARTS_NUMBER
import com.husiev.universalcharts.utils.CSV_CELL_SEPARATOR
import java.lang.Exception

class ChartManager {
    val chartData = mutableListOf<SimpleChart>()
    val xAxisLabel = mutableListOf<String>()

    fun setChartData(data: Array<Array<String>>?) {
        chartData.clear()
        data?.let {
            for (i in 0 until CHARTS_NUMBER) {
                chartData.add(SimpleChart("data0$i"))
            }
            for (i in it.indices) {
                for (j in it[i].indices) {
                    val x = i.toFloat()
                    val y = if (it[i][j] != "") {
                        chartData[j].skipCell += false
                        it[i][j].toFloat()
                    } else {
                        chartData[j].skipCell += true
                        0f
                    }
                    chartData[j].data.add(PointF(x, y))
                    xAxisLabel.add(j.toString())
                }
            }
        }
    }
}