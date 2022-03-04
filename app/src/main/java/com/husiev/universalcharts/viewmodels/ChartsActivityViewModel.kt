package com.husiev.universalcharts.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.husiev.universalcharts.UChartApplication
import com.husiev.universalcharts.DataRepository
import com.husiev.universalcharts.db.entity.ChartWithData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChartsActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DataRepository = (application as UChartApplication).repository
    var chartId: String = ""

    fun loadChartData(): LiveData<ChartWithData> {
        return repository.loadChartWithData(chartId)
    }
}