package com.husiev.universalcharts.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.data.CombinedData
import com.husiev.universalcharts.charts.ChartManager
import com.husiev.universalcharts.databinding.ActivityChartsBinding
import com.husiev.universalcharts.R
import com.husiev.universalcharts.utils.*
import com.husiev.universalcharts.viewmodels.ChartsActivityViewModel

class ChartsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChartsBinding
    private lateinit var model: ChartsActivityViewModel
    private val chartManager = ChartManager()
    private var chartID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChartsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setViewModel()
        setMenu()
        setTitle()
        initialChartAdjusting()
    }

    override fun onResume() {
        super.onResume()
        loadChartFromFile()
    }

    //<editor-fold desc="Common Initialization">
    private fun setViewModel() {
        model = ViewModelProvider(this)[ChartsActivityViewModel::class.java]
    }

    private fun setMenu() {
        setSupportActionBar(binding.toolbarCharts)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
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

        model.chartId = chartID
        model.getChartTitle().observe(this) {
            title = it
        }
    }

    private fun initialChartAdjusting() { chartManager.initialChartAdjusting(binding.combinedChartLayout, this) }
    //</editor-fold>

    private fun loadChartFromFile() {
        model.getChartData().observe(this) { data ->
            chartManager.setChartData(data)
            if (chartManager.chartData.isNotEmpty())
                prepareDataForChart()
            binding.combinedChartLayout.invalidate()
        }
    }

    private fun prepareDataForChart() {
        binding.combinedChartLayout.xAxis.valueFormatter = MyXAxisValueFormatter(chartManager.xAxisLabel)
        binding.combinedChartLayout.data = CombinedData().apply { this.setData(chartManager.getLineData()) }
    }

}