package com.husiev.universalcharts.viewmodels

import androidx.lifecycle.*
import com.husiev.universalcharts.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {
    var chartId: String = ""

    val allColors = repository.listOfColors

    fun getSettings() = repository.getListOfSettings(chartId)

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