package com.husiev.universalcharts.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.husiev.universalcharts.DataRepository
import com.husiev.universalcharts.UChartApplication
import com.husiev.universalcharts.charts.ChartManager.Companion.convertCsvToStringMatrix
import com.husiev.universalcharts.utils.EditTableRow
import com.husiev.universalcharts.utils.ExtIOData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditRowsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DataRepository = (application as UChartApplication).repository

    fun loadChartDataFromFile(context: Context, filename: String?): LiveData<List<EditTableRow>> {
        val tableRows = MutableLiveData<List<EditTableRow>>()
        viewModelScope.launch(Dispatchers.IO) {
            val rows = mutableListOf<EditTableRow>()
            val dataCsv = filename?.let { repository.getLinesFromFile(it) }
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

    fun saveChartData(filename: String?, data: ByteArray?, append: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveData(filename, data, append)
        }
    }

}