package com.husiev.universalcharts.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TableRow
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.husiev.universalcharts.R
import com.husiev.universalcharts.databinding.ActivityMainBinding
import com.husiev.universalcharts.utils.INTENT_CHART_ID
import com.husiev.universalcharts.utils.SelectionTableRowChartInfo
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

            model.setTable().observe(this) { chartList ->
                for (i in 0..chartList.lastIndex) {
                    table.addView(addRow(this, chartList[i].title, chartList[i].uid))
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

    private fun addRow(context: Context, title: String, id: String) : TableRow {
        return TableRow(context).apply {
            layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setPadding(0, 0, 0, 0)
            addView(SelectionTableRowChartInfo(context).apply { setTitle(title) })
            tag = id
            setBackgroundResource(R.drawable.selector_tablerow_highlighter)
            setOnClickListener {
                val intent = Intent(context, ChartsActivity().javaClass)
                intent.putExtra(INTENT_CHART_ID, id)
                context.startActivity(intent)
            }
            setOnLongClickListener {
                binding.tableAllCharts.tag = id
                removeChartDialog.show()
                return@setOnLongClickListener true
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
                model.setTable().observe(this@SelectionActivity) { chartList ->
                    for (i in 0..chartList.lastIndex) {
                        binding.tableAllCharts.addView(addRow(this@SelectionActivity, chartList[i].title, chartList[i].uid))
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
                        binding.tableAllCharts.addView(addRow(this@SelectionActivity, chartTitle, chartId))
                    }
                }
            }
            setNegativeButton(R.string.alert_dialog_button_cancel) { _, _ -> }
        }
        newChartDialog = dialog.create()
    }
    //</editor-fold>
}