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
import com.husiev.universalcharts.ChartApplication
import com.husiev.universalcharts.DataRepository
import com.husiev.universalcharts.R
import com.husiev.universalcharts.ui.ChartsActivity
import com.husiev.universalcharts.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SelectionRowsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DataRepository = (application as ChartApplication).repository

    fun setTable(context: Context): LiveData<List<TableRow>> {
        val tableRows = MutableLiveData<List<TableRow>>()
        viewModelScope.launch(Dispatchers.IO) {
            val rows = mutableListOf<TableRow>()
            val chartID = repository.listOfDirectories
            if (chartID != null && chartID.isNotEmpty()) {
                for (i in 0..chartID.lastIndex) {
                    rows.add(addRow(context, getChartTitle(chartID[i]), chartID[i]))
                }
                tableRows.postValue(rows)
            }
        }
        return tableRows
    }

    fun addRow(context: Context, title: String, id: String) : TableRow {
        return TableRow(context).apply {
            layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setPadding(0, 0, 0, 0)
            addView(SelectionTableRowChartInfo(context).apply { setTitle(title) })
            tag = id
            setBackgroundResource(R.drawable.selector_tablerow_highlighter)
            setOnClickListener {
                val intent = Intent(context, ChartsActivity().javaClass)
                intent.putExtra(INTENT_CHART_ID, id)
                context.startActivity(intent)
            }
        }
    }

    private fun getChartTitle(id: String?): String {
        return repository.getChartTitle(id)
    }
}
