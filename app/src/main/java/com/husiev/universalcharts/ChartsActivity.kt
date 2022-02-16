package com.husiev.universalcharts

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF
import com.husiev.universalcharts.charts.ChartManager
import com.husiev.universalcharts.databinding.ActivityChartsBinding
import com.husiev.universalcharts.utils.*
import com.husiev.universalcharts.viewmodels.SelectionRowsViewModel.Companion.getTitle

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
        initialChartAdjusting()
    }

    override fun onResume() {
        super.onResume()
        if (loadChartFromFile())
            prepareDataForChart()
        binding.combinedChartLayout.invalidate()
    }

    //<editor-fold desc="Common Initialization">
    private fun setMenu() {
        setSupportActionBar(binding.toolbarCharts)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when(item.itemId) {
            R.id.action_settings -> return true
            R.id.action_edit -> {
                val intent = Intent(this, EditActivity().javaClass)
                intent.putExtra(INTENT_CHART_ID, chartID)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setTitle() {
        if (chartID == null)
            chartID = intent.getStringExtra(INTENT_CHART_ID)
        getTitle(this, chartID).apply {
            title = this
        }
    }
    //</editor-fold>

    private fun loadChartFromFile(): Boolean {
        val dataCsv = ExtIOData.readLinesFromFile(this, chartID?.let { getActualFilename(it) })
        dataCsv?.let {
            chartManager.setChartData(it)
        }
        return chartManager.chartData.isNotEmpty()
    }

    //<editor-fold desc="Initial Chart Adjusting">
    private fun initialChartAdjusting() {
        setChartMixProperties()
        setChartMarker()
    }

    private fun setChartMixProperties() {
        binding.combinedChartLayout.apply {
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

    private fun setChartMarker() {
        val marker = MyMarkerView(this, R.layout.chart_marker_view_layout)
        binding.combinedChartLayout.apply {
            setMarker(marker)
            setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(e: Entry, h: Highlight) {
                    marker.refreshContent(e, h)
                }

                override fun onNothingSelected() {}
            })
        }
    }

    private class MyMarkerView(context: Context?, layoutResource: Int) : MarkerView(context, layoutResource) {
        private var tvContent: TextView? = null
        private var mOffset: MPPointF? = null

        init {
            // find layout components
            tvContent = findViewById(R.id.tvContent)
        }

        // callbacks everytime the MarkerView is redrawn,
        // can be used to update the content (user-interface)
        override fun refreshContent(e: Entry?, highlight: Highlight?) {
            super.refreshContent(e, highlight)
            tvContent?.text = "${e?.y}"
        }

        override fun getOffset(): MPPointF {
            if (mOffset == null) {
                // center the marker horizontally and vertically
                mOffset = MPPointF(-(width.toFloat() / 2), (-height).toFloat())
            }
            return mOffset as MPPointF
        }
    }
    //</editor-fold>

    //<editor-fold desc="Fulfilling chart with data">
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

        with(chartManager.chartData[index]) {
            for (i in 0 until data.size) {
                if (!skipCell[i])
                    entries.add(Entry(data[i].x, data[i].y))
            }
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
            return _values[value.toInt() + 1]
        }
    }
    //</editor-fold>

}