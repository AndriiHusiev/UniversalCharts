package com.husiev.universalcharts.utils

import android.content.Context
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TableRow
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.husiev.universalcharts.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class SelectionRowsViewModel : ViewModel() {

    fun setTable(context: Context): LiveData<List<TableRow>> {
        val tableRows = MutableLiveData<List<TableRow>>()
        viewModelScope.launch(Dispatchers.IO) {
            val rows = mutableListOf<TableRow>()
            val title = getListOfChartTitles(context)
            if (title != null) {
                for (i in 0..title.lastIndex) {
                    rows.add(addRow(context, title[i]))
                }
                tableRows.postValue(rows)
            }
        }
        return tableRows
    }

    private fun getListOfChartTitles(context: Context): List<String>? {
        val titleID = ExtIOData.getListOfDirs(context, "")

        return try {
            titleID.map { id ->
                val filename = "$id/$CHART_INFO_FILENAME$FILE_EXTENSION_CSV"
                val data = ExtIOData.readLinesFromFile(context, filename)
                if (data != null && data.size > 1 && data[1] != null)
                    data[1].substring(0, data[1].length-1)
                else
                    NOT_APPLICABLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }

    fun addRow(context: Context, title: String) : TableRow {
        return TableRow(context).apply {
            layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setPadding(0, 0, 0, 0)
            addView(SelectionTableRowChartInfo(context).apply { setTitle(title) })
            setBackgroundResource(R.drawable.selector_tablerow_highlighter)
            setOnClickListener({
                // start next Activity
            })
//            setOnLongClickListener({ // Remove chart })
        }
    }
}