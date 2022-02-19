package com.husiev.universalcharts.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.husiev.universalcharts.ChartApplication
import com.husiev.universalcharts.DataRepository

class ChartsActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DataRepository = (application as ChartApplication).repository

    fun getChartTitle(id: String?): String {
        return repository.getChartTitle(id)
    }
}