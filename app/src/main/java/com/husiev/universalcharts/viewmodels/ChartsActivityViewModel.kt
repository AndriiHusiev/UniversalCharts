package com.husiev.universalcharts.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.husiev.universalcharts.DataRepository
import com.husiev.universalcharts.db.entity.SimpleChartData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChartsActivityViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {
    var chartId: String = ""

    val allColors = repository.listOfColors

    fun loadChartData() = repository.loadChartWithData(chartId)

    fun saveDataOnFilesystem(data: List<SimpleChartData>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveDataOnFilesystem(chartId, data)
        }
    }
}