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
import com.husiev.universalcharts.db.entity.ChartDataEntity
import com.husiev.universalcharts.utils.CHARTS_NUMBER
import com.husiev.universalcharts.utils.COLOR_TEXT_DARK
import com.husiev.universalcharts.utils.MyMarkerView
import com.husiev.universalcharts.utils.logDebugOut

class ChartManager {
    private val chartColor = listOf(Color.BLACK, Color.BLUE, Color.GREEN, Color.RED, Color.GRAY)
    private val _chartData = mutableListOf<SimpleChart>()
    private val _xAxisLabel = mutableListOf<String>()

    val isNotEmptyDataList: Boolean get() = _chartData.isNotEmpty()
    val xAxisLabels: List<String> get() = _xAxisLabel

    fun setChartData(data: List<ChartDataEntity>) {
        _chartData.clear()
        _xAxisLabel.clear()

        if (data.isNotEmpty()) {
            for (i in 0 until CHARTS_NUMBER) {
                _chartData.add(SimpleChart("data0$i"))
            }

            for (i in data.indices) {
                data[i].data?.let {
                    setPointData(0, i, it.chartData1)
                    setPointData(1, i, it.chartData2)
                    setPointData(2, i, it.chartData3)
                    setPointData(3, i, it.chartData4)
                    setPointData(4, i, it.chartData5)
                    _xAxisLabel.add((i+1).toString())
                }
            }
        }
    }

    private fun setPointData(index: Int, xValue: Int, yValue: Float?) {
        val x = xValue.toFloat()
        val y =  if (yValue != null) {
            _chartData[index].skipCell += false
            yValue
        } else {
            _chartData[index].skipCell += true
            0f
        }
        _chartData[index].data.add(PointF(x, y))
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

        with(_chartData[index]) {
            for (i in 0 until data.size) {
                if (!skipCell[i])
                    entries.add(Entry(data[i].x, data[i].y))
            }
        }

        return LineDataSet(entries, _chartData[index].label).apply {
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