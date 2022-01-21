package com.husiev.universalcharts

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.husiev.universalcharts.databinding.ActivityMainBinding
import com.husiev.universalcharts.utils.*
import com.husiev.universalcharts.utils.CSV_CELL_SEPARATOR
import java.util.*

class SelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var newChartDialog: AlertDialog
    private lateinit var removeChartDialog: AlertDialog
    private val model = SelectionRowsViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setWidgets()
        setNewChartDialog()
        setRemoveChartDialog()
    }

    override fun onResume() {
        super.onResume()
        binding.tableAllCharts.let { table ->
            table.removeAllViews()

            model.setTable(this).observe(this, {rows ->
                for (row in rows) {
                    row.setOnLongClickListener(setLongClickListener(row.tag as String))
                    table.addView(row)
                }
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

    private fun setRemoveChartDialog() {
        val title = resources.getString(R.string.alert_dialog_delete_chart_header)
        val message = resources.getString(R.string.alert_dialog_list_item_remove_confirm)

        val dialog = AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setCancelable(true)
            setPositiveButton(R.string.alert_dialog_button_ok) { _, _ ->
                ExtIOData.deleteDir(this@SelectionActivity, binding.tableAllCharts.tag as String?)
                binding.tableAllCharts.removeAllViews()
                model.setTable(this@SelectionActivity).observe(this@SelectionActivity, {rows ->
                    for (row in rows) {
                        row.setOnLongClickListener(setLongClickListener(row.tag as String))
                        binding.tableAllCharts.addView(row)
                    }
                })
            }
            setNegativeButton(R.string.alert_dialog_button_cancel) { _, _ -> }
        }
        removeChartDialog = dialog.create()
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
                    val chartID = createNewChart(chartTitle)
                    binding.tableAllCharts.addView(model.addRow(this@SelectionActivity, chartTitle, chartID).apply {
                        this.setOnLongClickListener(setLongClickListener(this.tag as String))
                    })
                }
            }
            setNegativeButton(R.string.alert_dialog_button_cancel) { _, _ -> }
        }
        newChartDialog = dialog.create()
    }

    private fun createNewChart(chartName: String): String {
        // Actual timestamp is used as directory name
        val dirName = Date().time.toString()
        // Create directory with specified name
        ExtIOData.getExternalStorageDir(this, dirName)
        val path = "$dirName/$CHART_INFO_FILENAME$FILE_EXTENSION_CSV"
        ExtIOData.saveDataToFile(this, path, prepareDataToSaving(chartName).toByteArray(), false)
        return dirName
    }

    private fun prepareDataToSaving(chartName: String): String {
        val data: StringBuilder = StringBuilder()
        data.append("Title").append(CSV_CELL_SEPARATOR).append(NEW_LINE)
            .append(chartName).append(CSV_CELL_SEPARATOR).append(NEW_LINE)
        return data.toString()
    }

    private fun setLongClickListener(id: String): View.OnLongClickListener {
        return View.OnLongClickListener{
            binding.tableAllCharts.tag = id
            removeChartDialog.show()
            return@OnLongClickListener true
        }
    }
    //</editor-fold>
}