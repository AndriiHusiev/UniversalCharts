package com.husiev.universalcharts.ui

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType.*
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.husiev.universalcharts.R
import com.husiev.universalcharts.databinding.ActivityEditBinding
import com.husiev.universalcharts.db.entity.ChartDataEntity
import com.husiev.universalcharts.utils.*
import com.husiev.universalcharts.viewmodels.EditRowsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    private val model: EditRowsViewModel by viewModels()
    private lateinit var newValueDialog: AlertDialog
    private lateinit var removeValueDialog: AlertDialog
    private lateinit var editTextForDialog: EditText
    private var tagCell: String = ""
    private var customTable = mutableListOf<EditTableRow>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()
        getIntentData()
        initActivityItems()
        setNewValueDialog()
        setRemoveValueDialog()
        setObserver()
    }

    //<editor-fold desc="Menu">
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbarEdit)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_add_row -> {
                model.addRowInLastPosition()
            }
            R.id.action_delete_row -> {
                if (customTable.isNotEmpty())
                    model.deleteLastRow(customTable.last().rowId)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    //</editor-fold>

    //<editor-fold desc="Initialization">
    private fun getIntentData() {
        intent.getStringExtra(INTENT_CHART_ID)?.let {
            model.chartId = it
        }
        logDebugOut("EditActivity", "getIntentData chartId", model.chartId)
    }

    private fun initActivityItems() {
        binding.tableEditChartData.setColumnStretchable(0, true)
        editTextForDialog = EditText(this).apply {
            setText("")
            inputType = TYPE_CLASS_NUMBER + TYPE_NUMBER_FLAG_DECIMAL + TYPE_NUMBER_FLAG_SIGNED
        }
    }

    private fun setObserver() {
        model.loadListOfChartData().observe(this) { data ->
//            logDebugOut("EditActivity", "setObserver() ChartDataEntity size", "${data.size}")
            fillCustomTable(data)
            fillTable()
        }
    }
    //</editor-fold>

    //<editor-fold desc="Edit Table">
    private fun fillCustomTable(chartData: List<ChartDataEntity>) {
        customTable.clear()
        for (i in chartData.indices) {
            customTable.add(addRow(i, chartData[i]))
        }
    }

    private fun fillTable() {
        binding.tableEditChartData.removeAllViews()
        customTable.forEach {
            binding.tableEditChartData.addView(it)
        }
    }

    private fun addRow(index: Int, entity: ChartDataEntity) : EditTableRow {
        val row = EditTableRow(this, entity).apply {
            rowIndex = index
            for (i in 0 until CHARTS_NUMBER) {
                this.setCellClickListener(setClickListener(rowIndex, i), i)
                this.setCellLongClickListener(setLongClickListener(rowIndex, i), i)
            }
        }
        return row
    }

    private fun setClickListener(rowIndex: Int, cellIndex: Int): View.OnClickListener {
        return View.OnClickListener {
            tagCell = tagOfEditTableCell(rowIndex, cellIndex)
            editTextForDialog.setText(customTable[rowIndex].getCell(cellIndex))
            editTextForDialog.setSelection(0, editTextForDialog.text.length)
            newValueDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
            newValueDialog.show()
        }
    }

    private fun setLongClickListener(rowIndex: Int, cellIndex: Int): View.OnLongClickListener {
        return View.OnLongClickListener {
            tagCell = tagOfEditTableCell(rowIndex, cellIndex)
            removeValueDialog.show()
            return@OnLongClickListener true
        }
    }

    private fun editTable(value: String) {
        val pos = getPointPosition(tagCell)
        with(customTable[pos.y]) {
            this.setCell(pos.x, value)
            model.editCell(this.toChartDataEntity(model.chartId))
        }
    }
    //</editor-fold>

    //<editor-fold desc="Dialogs">
    private fun setNewValueDialog() {
        val title = resources.getString(R.string.alert_dialog_header_enter_new_value)

        val dialog = AlertDialog.Builder(this).apply {
            setTitle(title)
            setView(editTextForDialog)
            setPositiveButton(R.string.alert_dialog_button_ok) { _, _ ->
                val newValue = editTextForDialog.text.toString()
                if (newValue != "") {
                    editTable(newValue)
                }
            }
            setNegativeButton(R.string.alert_dialog_button_cancel) { _, _ -> }
        }
        newValueDialog = dialog.create()
    }

    private fun setRemoveValueDialog() {
        val title = resources.getString(R.string.alert_dialog_header_delete_value)
        val message = resources.getString(R.string.alert_dialog_message_delete_value)

        val dialog = AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(R.string.alert_dialog_button_ok) { _, _ ->
                editTable("")
            }
            setNegativeButton(R.string.alert_dialog_button_cancel) { _, _ -> }
        }
        removeValueDialog = dialog.create()
    }
    //</editor-fold>
}