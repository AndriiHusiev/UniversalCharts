package com.husiev.universalcharts.utils

import android.content.Context
import android.widget.TableLayout
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SelectionRowsViewModel : ViewModel() {
    fun setRows(context: Context, rowTitle: List<String>?, table: TableLayout) {
        viewModelScope.launch(Dispatchers.IO) {
            if (rowTitle != null) {
                for (i in 0..rowTitle.lastIndex) {
                    addRow(context, rowTitle[i], table)
                }
            }
        }
    }

    fun addRow(context: Context, title: String, table: TableLayout) {
        table.addView(TextView(context).apply {
            text = title
            textSize = 30f
        })
    }
}