package com.husiev.universalcharts.viewmodels

import android.app.Application
import android.content.Context
import android.content.Intent
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TableRow
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.husiev.universalcharts.UChartApplication
import com.husiev.universalcharts.DataRepository
import com.husiev.universalcharts.R
import com.husiev.universalcharts.db.entity.ChartsEntity
import com.husiev.universalcharts.ui.ChartsActivity
import com.husiev.universalcharts.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SelectionRowsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DataRepository = (application as UChartApplication).repository

    fun setTable(): LiveData<List<ChartsEntity>> {
        val chartList = MutableLiveData<List<ChartsEntity>>()
        viewModelScope.launch(Dispatchers.IO) {
            val chart = mutableListOf<ChartsEntity>()
            val chartID = repository.listOfDirectories
            if (chartID != null && chartID.isNotEmpty()) {
                for (i in 0..chartID.lastIndex) {
                    chart.add(ChartsEntity(chartID[i], repository.getChartTitle(chartID[i])))
                }
                chartList.postValue(chart)
            }
        }
        return chartList
    }

    fun createNewChart(chartTitle: String): LiveData<String> {
        val chartId = MutableLiveData<String>()
        viewModelScope.launch(Dispatchers.IO) {
            chartId.postValue(repository.createNewChart(chartTitle))
        }
        return chartId
    }

    fun deleteChart(id: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteChart(id)
        }
    }
}
