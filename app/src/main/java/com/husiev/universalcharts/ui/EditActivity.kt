package com.husiev.universalcharts.ui

import android.app.AlertDialog
import android.graphics.Point
import android.os.Bundle
import android.text.InputType.*
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.husiev.universalcharts.R
import com.husiev.universalcharts.databinding.ActivityEditBinding
import com.husiev.universalcharts.utils.*
import com.husiev.universalcharts.viewmodels.EditRowsViewModel

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    private lateinit var model: EditRowsViewModel
    private lateinit var newValueDialog: AlertDialog
    private lateinit var removeValueDialog: AlertDialog
    private lateinit var editTextForDialog: EditText
    private var chartID: String? = null
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
    }

    override fun onResume() {
        super.onResume()

        if (customTable.isEmpty() ) {
            model.loadChartDataFromFile(this).observe(this) {
                fillTable(it)
            }
        }
    }

    //<editor-fold desc="Menu">
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
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
                addLastRow()
            }
            R.id.action_delete_row -> {
                with(binding.tableEditChartData) {
                    if (childCount > 0) {
                        deleteLastRow()
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
    //</editor-fold>

    //<editor-fold desc="Initialization">
    private fun getIntentData() {
        chartID = intent.getStringExtra("chartID")
        model.chartId = chartID
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

    private fun setListeners(row: EditTableRow, rowIndex: Int) {
        for (i in 0 until CHARTS_NUMBER) {
            row.getCell(i)?.setOnClickListener {
                tagCell = tagOfEditTableCell(rowIndex, i)
                editTextForDialog.setText("")
                newValueDialog.show()
            }
            row.getCell(i)?.setOnLongClickListener {
                tagCell = tagOfEditTableCell(rowIndex, i)
                removeValueDialog.show()
                return@setOnLongClickListener true
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="Edit Table">
    private fun fillTable(data: Array<Array<String>>?) {
        binding.tableEditChartData.removeAllViews()
        customTable.clear()
        if (data != null)
            for (i in data.indices) {
                customTable.add(addRow(i, data[i]))
                binding.tableEditChartData.addView(customTable[i])
            }
    }

    private fun addRow(index: Int, cells: Array<String>?) : EditTableRow {
        val row = EditTableRow(this).apply {
            rowIndex = index
            val data: Array<String> = cells ?: arrayOf("", "", "", "", "")
            for (i in data.indices) {
                this.setCell(i, data[i])
            }
        }
        setListeners(row, index)
        return row
    }

    private fun addLastRow() {
        model.addRowInLastPosition().observe(this) {
            if (it != null) {
                val index = it.lastIndex
                val row = addRow(index, it[index])
                customTable.add(row)
                binding.tableEditChartData.addView(row)
            }
        }
    }

    private fun deleteLastRow() {
        model.deleteLastRow().observe(this) {
            fillTable(it)
        }
    }

    private fun editTable(value: String, position: Point) {
        model.editCell(value, position).observe(this) {
            // Может не стоит при мелких изменениях обновлять с нуля всю таблицу?
            fillTable(it)
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
                if (editTextForDialog.text.toString() != "") {
                    val pos = getPointPosition(tagCell)
                    editTable(editTextForDialog.text.toString(), pos)
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
        }
        removeValueDialog = dialog.create()
    }
    //</editor-fold>
}