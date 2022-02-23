package com.husiev.universalcharts.charts

import android.content.Context
import android.graphics.Color
import android.graphics.PointF
import android.graphics.Typeface
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.husiev.universalcharts.R
import com.husiev.universalcharts.utils.CHARTS_NUMBER
import com.husiev.universalcharts.utils.COLOR_TEXT_DARK
import com.husiev.universalcharts.utils.MyMarkerView

class ChartManager {
    private val chartColor = listOf(Color.BLACK, Color.BLUE, Color.GREEN, Color.RED, Color.GRAY)

    val chartData = mutableListOf<SimpleChart>()
    val xAxisLabel = mutableListOf<String>()

    fun setChartData(data: Array<Array<String>>?) {
        chartData.clear()
        xAxisLabel.clear()
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
                }
                xAxisLabel.add((i+1).toString())
            }
        }
    }

    //<editor-fold desc="Initial Chart Adjusting">
    fun initialChartAdjusting(chart: CombinedChart, context: Context) {
        setChartMixProperties(chart)
        setChartMarker(chart, context)
    }

    private fun setChartMixProperties(chart: CombinedChart) {
        chart.apply {
            setScaleEnabled(false)
            setDrawBorders(true)
            setBorderColor(COLOR_TEXT_DARK)
            isDoubleTapToZoomEnabled = false
            isAutoScaleMinMaxEnabled = false
            description = Description().apply { text = "" }
            legend.textColor = COLOR_TEXT_DARK
            xAxis.apply {
                textColor = COLOR_TEXT_DARK
                position = XAxis.XAxisPosition.BOTTOM
                typeface = Typeface.DEFAULT_BOLD
                isGranularityEnabled = true
                granularity = 1F
            }
            axisLeft.apply {
                textColor = COLOR_TEXT_DARK
                typeface = Typeface.DEFAULT_BOLD
            }
            axisRight.apply {
                textColor = COLOR_TEXT_DARK
                typeface = Typeface.DEFAULT_BOLD
                spaceMin = 0F
            }
        }
    }

    private fun setChartMarker(chart: CombinedChart, context: Context) {
        val marker = MyMarkerView(context, R.layout.chart_marker_view_layout)
        chart.apply {
            setMarker(marker)
            setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(e: Entry, h: Highlight) {
                    marker.refreshContent(e, h)
                }

                override fun onNothingSelected() {}
            })
        }
    }
    //</editor-fold>

    //<editor-fold desc="Line Data Preparation">
    fun getLineData(): LineData {
        val lineData = LineData()
        for (i in 0 until CHARTS_NUMBER)
            lineData.addDataSet(getLineDataSet(i))
        return lineData
    }

    private fun getLineDataSet(index: Int): LineDataSet {
        val entries = mutableListOf<Entry>()

        with(chartData[index]) {
            for (i in 0 until data.size) {
                if (!skipCell[i])
                    entries.add(Entry(data[i].x, data[i].y))
            }
        }

        return LineDataSet(entries, chartData[index].label).apply {
            color = chartColor[index]
            lineWidth = 2f
            valueTextColor = COLOR_TEXT_DARK
            mode = LineDataSet.Mode.CUBIC_BEZIER
            cubicIntensity = 0.1f
            setDrawValues(false)
            axisDependency = YAxis.AxisDependency.LEFT
            isHighlightEnabled = true
            setCircleColor(chartColor[index])
            circleRadius = 4f
            circleHoleRadius = 2f
            setDrawCircles(true)
            setDrawCircleHole(true)
            isVisible = true
        }
    }
    //</editor-fold>
}