package com.husiev.universalcharts.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.husiev.universalcharts.UChartApplication
import com.husiev.universalcharts.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChartsActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DataRepository = (application as UChartApplication).repository
    var chartId: String? = null
        set(value) {
            if (value != null)
                field = value
        }

    fun getChartTitle(): LiveData<String> {
        val chartTitle = MutableLiveData<String>()
        viewModelScope.launch(Dispatchers.IO) {
            chartTitle.postValue(repository.getChartTitle(chartId))
        }
        return chartTitle
    }

    fun getChartData(): LiveData<Array<Array<String>>> {
        val chartData = MutableLiveData<Array<Array<String>>>()
        viewModelScope.launch(Dispatchers.IO) {
            chartData.postValue(repository.getChartData(chartId))
        }
        return chartData
    }
}