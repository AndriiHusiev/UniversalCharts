package com.husiev.universalcharts.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.husiev.universalcharts.DataRepository
import com.husiev.universalcharts.db.entity.ChartsEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectionRowsViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {

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
