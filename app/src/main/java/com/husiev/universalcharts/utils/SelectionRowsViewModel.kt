package com.husiev.universalcharts.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TableRow
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.husiev.universalcharts.ChartsActivity
import com.husiev.universalcharts.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class SelectionRowsViewModel : ViewModel() {

    fun setTable(context: Context): LiveData<List<TableRow>> {
        val tableRows = MutableLiveData<List<TableRow>>()
        viewModelScope.launch(Dispatchers.IO) {
            val rows = mutableListOf<TableRow>()
            val chartID = ExtIOData.getListOfDirs(context, "")
            if (chartID.isNotEmpty()) {
                for (i in 0..chartID.lastIndex) {
                    getTitle(context, chartID[i])?.let {
                        rows.add(addRow(context, it, chartID[i]))
                    }
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
//            setOnLongClickListener({ // Remove chart })
        }
    }
}

fun getTitle(context: Context, id: String?): String? {
    return try {
        val filename = "$id/$CHART_INFO_FILENAME$FILE_EXTENSION_CSV"
        val data = ExtIOData.readLinesFromFile(context, filename)
        if (data != null && data.size > 1 && data[1] != null)
            data[1].substring(0, data[1].length-1)
        else
            null
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}