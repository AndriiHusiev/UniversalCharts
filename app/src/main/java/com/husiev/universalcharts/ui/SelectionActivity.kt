package com.husiev.universalcharts.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.husiev.universalcharts.R
import com.husiev.universalcharts.databinding.ActivityMainBinding
import com.husiev.universalcharts.viewmodels.SelectionRowsViewModel

class SelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var model: SelectionRowsViewModel
    private lateinit var newChartDialog: AlertDialog
    private lateinit var removeChartDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setViewModel()
        setWidgets()
        setNewChartDialog()
        setRemoveChartDialog()
    }

    override fun onResume() {
        super.onResume()
        binding.tableAllCharts.let { table ->
            table.removeAllViews()

            model.setTable(this).observe(this) { rows ->
                for (row in rows) {
                    row.setOnLongClickListener(setLongClickListener(row.tag as String))
                    table.addView(row)
                }
            }
        }
    }

    private fun setViewModel() {
        model = ViewModelProvider(this)[SelectionRowsViewModel::class.java]
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
        val title = resources.getString(R.string.alert_dialog_header_delete_chart)
        val message = resources.getString(R.string.alert_dialog_message_list_item_remove_confirm)

        val dialog = AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setCancelable(true)
            setPositiveButton(R.string.alert_dialog_button_ok) { _, _ ->
                model.deleteChart(binding.tableAllCharts.tag as String?)
                binding.tableAllCharts.removeAllViews()
                model.setTable(this@SelectionActivity).observe(this@SelectionActivity) { rows ->
                    for (row in rows) {
                        row.setOnLongClickListener(setLongClickListener(row.tag as String))
                        binding.tableAllCharts.addView(row)
                    }
                }
            }
            setNegativeButton(R.string.alert_dialog_button_cancel) { _, _ -> }
        }
        removeChartDialog = dialog.create()
    }

    //<editor-fold desc="NewChart Dialog">
    private fun setNewChartDialog() {
        val title = resources.getString(R.string.alert_dialog_header_choose_chart_name)
        val editText = EditText(this)

        val dialog = AlertDialog.Builder(this).apply {
            setTitle(title)
            setView(editText)
            setPositiveButton(R.string.alert_dialog_button_ok) { _, _ ->
                val chartTitle = editText.text.toString()
                if (chartTitle != "") {
                    model.createNewChart(chartTitle).observe(this@SelectionActivity) { chartId ->
                        binding.tableAllCharts.addView(model.addRow(this@SelectionActivity, chartTitle, chartId).apply {
                            this.setOnLongClickListener(setLongClickListener(this.tag as String))
                        })
                    }
                }
            }
            setNegativeButton(R.string.alert_dialog_button_cancel) { _, _ -> }
        }
        newChartDialog = dialog.create()
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