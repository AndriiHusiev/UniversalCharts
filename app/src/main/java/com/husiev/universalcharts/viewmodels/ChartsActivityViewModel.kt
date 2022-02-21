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

    fun getChartTitle(id: String?): LiveData<String> {
        val chartTitle = MutableLiveData<String>()
        viewModelScope.launch(Dispatchers.IO) {
            chartTitle.postValue(repository.getChartTitle(id))
        }
        return chartTitle
    }

    fun getChartData(id: String?): LiveData<List<String>> {
        val chartData = MutableLiveData<List<String>>()
        viewModelScope.launch(Dispatchers.IO) {
            id?.let { chartData.postValue(repository.getChartData(it)) }
        }
        return chartData
    }
}