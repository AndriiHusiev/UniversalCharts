package com.husiev.universalcharts.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.husiev.universalcharts.UChartApplication
import com.husiev.universalcharts.DataRepository
import com.husiev.universalcharts.db.entity.SimpleChartData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChartsActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DataRepository = (application as UChartApplication).repository
    var chartId: String = ""

    val allColors = repository.listOfColors

    fun loadChartData() = repository.loadChartWithData(chartId)

    fun saveDataOnFilesystem(data: List<SimpleChartData>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveDataOnFilesystem(chartId, data)
        }
    }
}