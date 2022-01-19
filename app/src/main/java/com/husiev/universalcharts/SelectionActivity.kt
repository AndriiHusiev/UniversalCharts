package com.husiev.universalcharts

import android.app.AlertDialog
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.husiev.universalcharts.databinding.ActivityMainBinding
import com.husiev.universalcharts.utils.*
import com.husiev.universalcharts.utils.CSV_CELL_SEPARATOR
import java.lang.Exception
import java.util.*

class SelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var newChartDialog: AlertDialog
    private val model = SelectionRowsViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setWidgets()
        setNewChartDialog()
    }

    override fun onResume() {
        super.onResume()
        binding.tableAllCharts.let {
            it.removeAllViews()

            model.setTable(this).observe(this, {rows ->
                for (row in rows)
                    it.addView(row)
            })
        }
    }

    private fun setWidgets() {
        binding.apply {
            setSupportActionBar(toolbarSelection)
            tableAllCharts.setColumnStretchable(0, true)

            fab.setOnClickListener {
                newChartDialog.show()
            }

            scrollViewSelectionTable.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                if (scrollY - oldScrollY > 0)
                    fab.hide()
                else
                    fab.show()
            }
        }
    }

    //<editor-fold desc="NewChart Dialog">
    private fun setNewChartDialog() {
        val title = resources.getString(R.string.alert_dialog_choose_chart_name)
        val editText = EditText(this)

        val dialog = AlertDialog.Builder(this).apply {
            setTitle(title)
            setView(editText)
            setPositiveButton(R.string.alert_dialog_button_ok) { _, _ ->
                val chartTitle = editText.text.toString()
                if (chartTitle != "") {
                    createNewChart(chartTitle)
                    binding.tableAllCharts.addView(model.addRow(this@SelectionActivity, chartTitle))
                }
            }
            setNegativeButton(R.string.alert_dialog_button_cancel) { _, _ -> }
        }
        newChartDialog = dialog.create()
    }

    private fun createNewChart(chartName: String) {
        // Actual timestamp is used as directory name
        val directory = Date().time.toString()
        // Create directory with specified name
        ExtIOData.getExternalStorageDir(this, directory)
        val path = "$directory/$CHART_INFO_FILENAME$FILE_EXTENSION_CSV"
        ExtIOData.saveDataToFile(this, path, prepareDataToSaving(chartName).toByteArray(), false)
    }

    private fun prepareDataToSaving(chartName: String): String {
        val data: StringBuilder = StringBuilder()
        data.append("Title").append(CSV_CELL_SEPARATOR).append(NEW_LINE)
            .append(chartName).append(CSV_CELL_SEPARATOR).append(NEW_LINE)
        return data.toString()
    }
    //</editor-fold>
}