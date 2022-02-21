package com.husiev.universalcharts.charts

import android.graphics.PointF
import android.util.Log
import com.husiev.universalcharts.utils.CHARTS_NUMBER
import com.husiev.universalcharts.utils.CSV_CELL_SEPARATOR
import java.lang.Exception

class ChartManager {
    val chartData = mutableListOf<SimpleChart>()
    val xAxisLabel = mutableListOf<String>()

    fun setChartData(lines: List<String>?) {
        chartData.clear()
        val data = convertCsvToStringMatrix(lines)
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

    companion object {
        fun convertCsvToStringMatrix(data: List<String>?): Array<Array<String>>? {
            if (data == null) return null

            var chartData = arrayOf<Array<String>>()

            try {
                data.forEach { line ->
                    var cell = ""
                    var cells = arrayOf<String>()

                    for (i in line.indices) {
                        if (line[i] == CSV_CELL_SEPARATOR) {
                            cells += cell
                            cell = ""
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
    }
}