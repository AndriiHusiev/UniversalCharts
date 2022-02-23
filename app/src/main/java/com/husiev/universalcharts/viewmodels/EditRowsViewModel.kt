package com.husiev.universalcharts.viewmodels

import android.app.Application
import android.content.Context
import android.graphics.Point
import androidx.lifecycle.*
import com.husiev.universalcharts.DataRepository
import com.husiev.universalcharts.UChartApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditRowsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DataRepository = (application as UChartApplication).repository
    private var chartData: Array<Array<String>> = emptyArray()
    var chartId: String? = null
        set(value) {
            if (value != null)
                field = value
        }

    fun loadChartDataFromFile(context: Context): LiveData<Array<Array<String>>> {
        val liveChartData = MutableLiveData<Array<Array<String>>>()

        viewModelScope.launch(Dispatchers.IO) {
            chartData = repository.getChartData(chartId)
            liveChartData.postValue(repository.getChartData(chartId))
        }
        return liveChartData
    }

    fun addRowInLastPosition(): LiveData<Array<Array<String>>> {
        val liveChartData = MutableLiveData<Array<Array<String>>>()
        viewModelScope.launch(Dispatchers.IO) {
            chartData += arrayOf("", "", "", "", "")
            if (repository.saveChartData(chartId, chartData))
                liveChartData.postValue(chartData)
        }
        return liveChartData
    }

    fun deleteLastRow(): LiveData<Array<Array<String>>> {
        val liveChartData = MutableLiveData<Array<Array<String>>>()
        var remainingData = arrayOf<Array<String>>()
        viewModelScope.launch(Dispatchers.IO) {
            for (i in 0 until  chartData.lastIndex) {
                remainingData += chartData[i]
            }
            chartData = remainingData
            if (repository.saveChartData(chartId, remainingData))
                liveChartData.postValue(remainingData)
        }
        return liveChartData
    }

    fun saveChartData(data: Array<Array<String>>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveChartData(chartId, data)
        }
    }

    fun editCell(value: String, position: Point): LiveData<Array<Array<String>>> {
        val liveChartData = MutableLiveData<Array<Array<String>>>()
        viewModelScope.launch(Dispatchers.IO) {
            chartData[position.y][position.x] = value
            repository.saveChartData(chartId, chartData)
            liveChartData.postValue(chartData)
        }
        return liveChartData
    }
}