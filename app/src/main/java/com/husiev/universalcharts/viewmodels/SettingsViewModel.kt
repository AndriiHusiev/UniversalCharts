package com.husiev.universalcharts.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.husiev.universalcharts.DataRepository
import com.husiev.universalcharts.UChartApplication
import com.husiev.universalcharts.db.entity.SettingsKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val chartId: String,
    application: Application
) : ViewModel() {
    private val repository: DataRepository = (application as UChartApplication).repository
    var keys: LiveData<List<SettingsKey>> = repository.getListOfKeys(chartId)
        private set

    fun getSettingsOfChart(index: Int) = repository.getListOfSettings(chartId, index)

    fun updateSettings(chartTitle: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertChart(chartTitle)
        }
    }

}

class SettingsModelFactory(
    private val chartId: String,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(chartId, application) as T
    }
}