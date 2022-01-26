package com.husiev.universalcharts

import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.husiev.universalcharts.charts.ChartManager
import com.husiev.universalcharts.charts.SimpleChart
import com.husiev.universalcharts.databinding.ActivityChartsBinding
import com.husiev.universalcharts.utils.CHARTS_NUMBER
import com.husiev.universalcharts.utils.COLOR_TEXT_DARK
import com.husiev.universalcharts.utils.INTENT_CHART_ID
import com.husiev.universalcharts.utils.getTitle
import kotlin.random.Random

class ChartsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChartsBinding
    private val chartManager = ChartManager()
    private var chartID: String? = null
    private val chartColor = listOf(Color.BLACK, Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChartsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setMenu()
        setTitle()
    }

    override fun onResume() {
        super.onResume()
        createFakeChartData()
        prepareDataForChart()
        binding.combinedChartLayout.invalidate()
    }

    //<editor-fold desc="Common Initialization">
    private fun setMenu() {
        setSupportActionBar(binding.toolbarCharts)
    }

    private fun setTitle() {
        if (chartID == null)
            chartID = intent.getStringExtra(INTENT_CHART_ID)
        getTitle(this, chartID).apply {
            title = this
        }
    }
    //</editor-fold>

    private fun createFakeChartData() {
        val COUNT_OF_POINTS = 6
        val random = Random

        for (i in 0 until CHARTS_NUMBER) {
            val data = List(COUNT_OF_POINTS) { PointF() }
            for (j in 0 until COUNT_OF_POINTS) {
                data[j].set(j.toFloat() + 1.2f, 100f * random.nextFloat())
                chartManager.xAxisLabel.add(j.toString())
            }
            chartManager.chartData.add(SimpleChart("data_0$i", data))
        }
    }

    private fun prepareDataForChart() {
        binding.combinedChartLayout.xAxis.valueFormatter = MyXAxisValueFormatter(chartManager.xAxisLabel)
        binding.combinedChartLayout.data = CombinedData().apply { this.setData(prepareLineData()) }
    }

    //<editor-fold desc="Line Data Preparation">
    private fun prepareLineData(): LineData {
        val lineData = LineData()
        for (i in 0 until CHARTS_NUMBER)
            lineData.addDataSet(getLineDataSet(i))
        return lineData
    }

    private fun getLineDataSet(index: Int): LineDataSet {
        val entries = mutableListOf<Entry>()

        for (i in 0 until chartManager.chartData[index].data.size) {
            entries.add(i, Entry(chartManager.chartData[index].data[i].x,chartManager.chartData[index].data[i].y))
        }

        return LineDataSet(entries, chartManager.chartData[index].label).apply {
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

    private class MyXAxisValueFormatter(values: List<String>): ValueFormatter() {
        private val _values: List<String> = values

        override fun getFormattedValue(value: Float): String {
            return _values[value.toInt() - 1]
        }
    }

}