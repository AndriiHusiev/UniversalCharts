package com.husiev.universalcharts.ui

import android.app.AlertDialog
import android.graphics.Point
import android.os.Bundle
import android.text.InputType.*
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.husiev.universalcharts.R
import com.husiev.universalcharts.databinding.ActivityEditBinding
import com.husiev.universalcharts.db.entity.ChartDataEntity
import com.husiev.universalcharts.db.entity.SimpleChartData
import com.husiev.universalcharts.utils.*
import com.husiev.universalcharts.viewmodels.EditRowsViewModel

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    private lateinit var model: EditRowsViewModel
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
        setViewModel()
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
                if (binding.tableEditChartData.childCount > 0) {
                    val row = fillEntity(customTable.last())
                    model.deleteLastRow(row)
                }
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

    private fun setViewModel() {
        model = ViewModelProvider(this)[EditRowsViewModel::class.java]
    }

    private fun setObserver() {
        model.loadListOfChartData().observe(this) { data ->
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
            editTextForDialog.setText("")
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

    private fun editTable(value: String, position: Point) {
        with(customTable[position.y]) {
            this.setCell(position.x, value)
            val row = fillEntity(this)
            model.editCell(row)
        }
    }

    private fun fillEntity(editTableRow: EditTableRow): ChartDataEntity {
        with(editTableRow) {
            val data = SimpleChartData(
                this.getCell(0).toFloatOrNull(),
                this.getCell(1).toFloatOrNull(),
                this.getCell(2).toFloatOrNull(),
                this.getCell(3).toFloatOrNull(),
                this.getCell(4).toFloatOrNull()
            )
            return ChartDataEntity(rowId, model.chartId, data)
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
                    val pos = getPointPosition(tagCell)
                    editTable(newValue, pos)
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
                val pos = getPointPosition(tagCell)
                editTable("", pos)
            }
            setNegativeButton(R.string.alert_dialog_button_cancel) { _, _ -> }
        }
        removeValueDialog = dialog.create()
    }
    //</editor-fold>
}