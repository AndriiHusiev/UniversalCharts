package com.husiev.universalcharts.utils

import android.content.Context
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.husiev.universalcharts.R
import com.husiev.universalcharts.databinding.ItemListOfChartsBinding

class SelectionTableRowChartInfo(context: Context) : RelativeLayout(context) {
    private var binding: ItemListOfChartsBinding

    init {
        val mInflater = LayoutInflater.from(context)
        val relativeLayout = mInflater.inflate(
            R.layout.item_list_of_charts,
            this,
            true
        ) as RelativeLayout
        binding = ItemListOfChartsBinding.bind(relativeLayout)
        binding.textChartTitle.text = ""
    }

    fun setTitle(title: String) {
        binding.textChartTitle.text = title
    }
}