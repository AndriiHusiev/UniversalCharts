package com.husiev.universalcharts.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.husiev.universalcharts.charts.convertCsvToStringMatrix
import com.husiev.universalcharts.utils.EditTableRow
import com.husiev.universalcharts.utils.ExtIOData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditRowsViewModel : ViewModel() {

    fun loadChartDataFromFile(context: Context, filename: String?): LiveData<List<EditTableRow>> {
        val tableRows = MutableLiveData<List<EditTableRow>>()
        viewModelScope.launch(Dispatchers.IO) {
            val rows = mutableListOf<EditTableRow>()
            val dataCsv = ExtIOData.readLinesFromFile(context, filename)
            dataCsv?.let { lines ->
                convertCsvToStringMatrix(lines)?.let {
                    for (i in it.indices) {
                        rows.add(addRow(context, i, it[i]))
                    }
                    tableRows.postValue(rows)
                }
            }
        }
        return tableRows
    }

    fun addRow(context: Context, index: Int, cells: Array<String>?) : EditTableRow {
        return EditTableRow(context).apply {
            rowIndex = index
            val data: Array<String> = cells ?: arrayOf("", "", "", "", "")
            for (i in data.indices) {
                this.setCell(i, data[i])
            }
        }
    }

}