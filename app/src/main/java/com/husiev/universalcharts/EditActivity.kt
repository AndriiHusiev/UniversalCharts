package com.husiev.universalcharts

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType.*
import android.view.Menu
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.husiev.universalcharts.databinding.ActivityEditBinding
import com.husiev.universalcharts.utils.*

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    private lateinit var newValueDialog: AlertDialog
    private lateinit var removeValueDialog: AlertDialog
    private var chartID: String? = null
    private var chartDataFilename: String? = null
    private var tagCell: String = ""
    private var customTable: List<EditTableRow> = mutableListOf()
    private val model = EditRowsViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()
        getIntentData()
        initActivityItems()
        setNewValueDialog()
        setRemoveValueDialog()
    }

    override fun onResume() {
        super.onResume()

        if (customTable.isEmpty() ) {
            model.loadChartDataFromFile(this, chartDataFilename).observe(this) {rows ->
                customTable = rows
            }
        }

        binding.tableEditChartData.removeAllViews()
        for (i in customTable.indices) {
            for (j in 0 until CHARTS_NUMBER) {
                customTable[i].getCell(j)?.apply {
                    setOnClickListener {
                        tagCell = tagOfEditTableCell(i, j)
                        newValueDialog.show()
                    }
                    setOnLongClickListener {
                        tagCell = tagOfEditTableCell(i, j)
                        removeValueDialog.show()
                        return@setOnLongClickListener true
                    }
                }
            }
            binding.tableEditChartData.addView(customTable[i])
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
    //</editor-fold>

    //<editor-fold desc="Initialization">
    private fun getIntentData() {
        chartID = intent.getStringExtra("chartID")
        chartDataFilename = chartID?.let { getActualFilename(it) }
    }

    private fun initActivityItems() {
        binding.tableEditChartData.setColumnStretchable(0, true)
    }
    //</editor-fold>

    //<editor-fold desc="Dialogs">
    private fun setNewValueDialog() {
        val title = resources.getString(R.string.alert_dialog_header_enter_new_value)
        val editText = EditText(this).apply {
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