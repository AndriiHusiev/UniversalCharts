package com.husiev.universalcharts.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.husiev.universalcharts.UChartApplication
import com.husiev.universalcharts.DataRepository
import com.husiev.universalcharts.db.entity.ChartsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SelectionRowsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DataRepository = (application as UChartApplication).repository

    val allCharts: LiveData<List<ChartsEntity>> = repository.listOfCharts

    fun createNewChart(chartTitle: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertChart(chartTitle)
        }
    }

    fun deleteChart(id: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteChart(id)
        }
    }
}
