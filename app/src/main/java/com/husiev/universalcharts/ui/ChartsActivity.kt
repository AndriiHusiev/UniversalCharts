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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChartsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setViewModel()
        setMenu()
        getIntentData()
        initialChartAdjusting()
        setObserver()
    }

    override fun onPause() {
        super.onPause()

        // this means that this activity will not be recreated now, user is leaving it
        // or the activity is otherwise finishing
        if (isFinishing)
            if (chartManager.isNotEmptyDataList) {
                model.saveDataOnFilesystem(chartManager.dataForExport)
            }
    }

    //<editor-fold desc="Common Initialization">
    private fun setViewModel() {
        model = ViewModelProvider(this)[ChartsActivityViewModel::class.java]
    }

    private fun setMenu() {
        setSupportActionBar(binding.toolbarCharts)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity().javaClass)
                intent.putExtra(INTENT_CHART_ID, model.chartId)
                startActivity(intent)
            }
            R.id.action_edit -> {
                val intent = Intent(this, EditActivity().javaClass)
                intent.putExtra(INTENT_CHART_ID, model.chartId)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getIntentData() {
        intent.getStringExtra(INTENT_CHART_ID)?.let {
            model.chartId = it
        }
    }

    private fun initialChartAdjusting() {
        chartManager.initialChartAdjusting(binding.combinedChartLayout, this)
    }

    private fun setObserver() {
        model.allColors.observe(this) {
            chartManager.setColorsList(it)
        }

        model.loadChartData().observe(this) {
            title = it.chart.title
            logDebugOut("ChartsActivity", "setObserver ChartDataEntity size", "${it.data.size}")
            chartManager.setChartData(it)
            if (chartManager.isNotEmptyDataList){
                binding.combinedChartLayout.xAxis.valueFormatter = MyXAxisValueFormatter(chartManager.xAxisLabels)
                binding.combinedChartLayout.data = CombinedData().apply { this.setData(chartManager.getLineData()) }
            } else {
                binding.combinedChartLayout.clear()
            }
            binding.combinedChartLayout.invalidate()
        }
    }
    //</editor-fold>

}