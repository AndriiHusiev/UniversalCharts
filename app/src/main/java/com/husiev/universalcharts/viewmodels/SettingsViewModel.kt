package com.husiev.universalcharts.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.husiev.universalcharts.DataRepository
import com.husiev.universalcharts.UChartApplication
import com.husiev.universalcharts.db.entity.SettingsEntity
import com.husiev.universalcharts.db.entity.SettingsKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val chartId: String,
    application: Application
) : ViewModel() {
    private val repository: DataRepository = (application as UChartApplication).repository

    val allColors = repository.listOfColors

    var keys: LiveData<List<SettingsKey>> = repository.getListOfKeys(chartId)
        private set

    fun getSettingsOfChart(index: Int) = repository.getListOfSettings(chartId, index)

    fun updateSettings(entity: SettingsEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateSettings(entity)
        }
    }

    fun updateField(
        uid: Int,
        label: String? = null,
        isVisible: Boolean? = null,
        showDots: Boolean? = null,
        curved: Boolean? = null,
        color: Int? = null,
        lineWidth: Int? = null
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateField(uid, label, isVisible, showDots, curved, color, lineWidth)
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