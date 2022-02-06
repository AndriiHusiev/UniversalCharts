package com.husiev.universalcharts.charts

import android.graphics.PointF
import com.husiev.universalcharts.utils.CSV_CELL_SEPARATOR
import java.lang.Exception

class ChartManager {
    val chartData = mutableListOf<SimpleChart>()
    val xAxisLabel = mutableListOf<String>()

    fun setChartData(data: Array<Array<String>>) {
        data.forEach {
            val line = mutableListOf<PointF>()
            for (i in it.indices) {
                line.add(PointF(i.toFloat() + 1, it[i].toFloat()))
            }
            chartData.add(SimpleChart("data", line))
        }
    }
}

fun convertCsvToStringMatrix(data: List<String>): Array<Array<String>>? {
    var chartData = arrayOf<Array<String>>()

    try {
        data.forEach { line ->
            var cell = ""
            var cells = arrayOf<String>()

            for (i in line.indices) {
                if (line[i] == CSV_CELL_SEPARATOR) {
                    cells += cell
                }
                else {
                    cell += line[i]
                }
            }
            chartData += cells
        }
        return chartData
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}