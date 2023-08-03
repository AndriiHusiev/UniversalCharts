package com.husiev.universalcharts.viewmodels

import androidx.lifecycle.*
import com.husiev.universalcharts.DataRepository
import com.husiev.universalcharts.db.entity.ChartDataEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditRowsViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {
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