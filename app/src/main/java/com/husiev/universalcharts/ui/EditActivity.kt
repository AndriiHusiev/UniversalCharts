package com.husiev.universalcharts.ui

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType.*
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
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
    private var chartID: String? = null
    private var tagCell: String = ""
    private var customTable = mutableListOf<EditTableRow>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()
        getIntentData()
        initActivityItems()
        setViewModel()
        setNewValueDialog()
        setRemoveValueDialog()
    }

    override fun onResume() {
        super.onResume()

        if (customTable.isEmpty() ) {
            model.loadChartDataFromFile(this, chartID).observe(this) {rows ->
                customTable = rows as MutableList<EditTableRow>
                binding.tableEditChartData.removeAllViews()
                for (i in customTable.indices) {
                    setListeners(customTable[i], i)
                    binding.tableEditChartData.addView(customTable[i])
                }
            }
        }

    }

    override fun onPause() {
        super.onPause()

        // this means that this activity will not be recreated now, user is leaving it
        // or the activity is otherwise finishing
        if (isFinishing)
            if (customTable.isNotEmpty()) {
                var data = arrayOf<Array<String>>()
                for (row in customTable) {
                    data += row.getCellsAsArray()
                }
                model.saveChartData(data)
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
                with(binding.tableEditChartData) {
                    val row = model.addRow(this@EditActivity, childCount, null)
                    setListeners(row, childCount)
                    customTable.add(row)
                    addView(row)
                }
            }
            R.id.action_delete_row -> {
                with(binding.tableEditChartData) {
                    if (childCount > 0) {
                        val child = getChildAt(childCount - 1)
                        (child as ViewGroup).removeAllViews()
                        removeViewAt(childCount-1)
                        customTable.removeAt(customTable.lastIndex)
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
    }

    private fun initActivityItems() {
        binding.tableEditChartData.setColumnStretchable(0, true)
    }

    private fun setViewModel() {
        model = ViewModelProvider(this)[EditRowsViewModel::class.java]
    }

    private fun setListeners(row: EditTableRow, rowIndex: Int) {
        for (i in 0 until CHARTS_NUMBER) {
            row.getCell(i)?.setOnClickListener {
                tagCell = tagOfEditTableCell(rowIndex, i)
//                (newValueDialog.listView[0] as EditText).setText("")
//                findViewById<EditText>(1100011.toInt()).setText("")
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

    //<editor-fold desc="Dialogs">
    private fun setNewValueDialog() {
        val title = resources.getString(R.string.alert_dialog_header_enter_new_value)
        val editText = EditText(this).apply {
            setText("")
            inputType = TYPE_CLASS_NUMBER + TYPE_NUMBER_FLAG_DECIMAL + TYPE_NUMBER_FLAG_SIGNED
        }

        val dialog = AlertDialog.Builder(this).apply {
            setTitle(title)
            setView(editText)
            setPositiveButton(R.string.alert_dialog_button_ok) { _, _ ->
                if (editText.text.toString() != "") {
                    val pos = getPointPosition(tagCell)
                    customTable[pos.y].getCell(pos.x)?.text = editText.text.toString()
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
                customTable[pos.y].getCell(pos.x)?.text = ""
            }
        }
        removeValueDialog = dialog.create()
    }
    //</editor-fold>
}