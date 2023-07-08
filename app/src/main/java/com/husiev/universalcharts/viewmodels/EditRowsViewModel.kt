package com.husiev.universalcharts.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.husiev.universalcharts.DataRepository
import com.husiev.universalcharts.UChartApplication
import com.husiev.universalcharts.db.entity.ChartDataEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditRowsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DataRepository = (application as UChartApplication).repository
    var chartId: String = ""

    fun loadListOfChartData(): LiveData<List<ChartDataEntity>> {
        return repository.loadListOfChartData(chartId)
    }

    fun addRowInLastPosition() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertNewRowTo(chartId)
        }
    }

    fun deleteLastRow(rowId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteLastRowOf(chartId, rowId)
        }
    }

    fun editCell(row: ChartDataEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateRow(row)
        }
    }
}