package com.husiev.universalcharts

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.husiev.universalcharts.databinding.ActivityMainBinding

class SelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var newChartDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setWidgets()
        setNewChartDialog()
    }

    private fun setWidgets() {
        binding.apply {
            setSupportActionBar(toolbarSelection)
            tableAllCharts.setColumnStretchable(0, true)
            fab.setOnClickListener { view ->
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

    private fun setNewChartDialog() {
        val title = resources.getString(R.string.alert_dialog_choose_chart_name)
        val editText = EditText(this)

        val dialog = AlertDialog.Builder(this).apply {
            setTitle(title)
            setView(editText)
            setPositiveButton(R.string.alert_dialog_button_ok) { _, _ ->
                val chartTitle = editText.text.toString()
                if (chartTitle != "") {
                    binding.tableAllCharts.addView(addRow(chartTitle))
                }
            }
            setNegativeButton(R.string.alert_dialog_button_cancel) { _, _ -> }
        }
        newChartDialog = dialog.create()
    }

    private fun addRow(title: String): View {
        return TextView(this).apply {
            text = title
        }
    }
}