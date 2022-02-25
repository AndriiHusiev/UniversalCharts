package com.husiev.universalcharts.utils

import android.content.Context
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.husiev.universalcharts.R
import com.husiev.universalcharts.databinding.ItemListOfChartsBinding
import com.husiev.universalcharts.db.entity.ChartsEntity

class SelectionTableRowChartInfo(context: Context) : RelativeLayout(context) {
    constructor(context: Context, data: ChartsEntity): this(context) {
        _data = data
        binding.textChartTitle.text = _data.title
        logDebugOut("SelectionTableRowChartInfo", "_data.title", _data.title)
    }

    private var _data: ChartsEntity = ChartsEntity("","")
    private var binding: ItemListOfChartsBinding

    init {
        val mInflater = LayoutInflater.from(context)
        val relativeLayout = mInflater.inflate(
            R.layout.item_list_of_charts,
            this,
            true
        ) as RelativeLayout
        binding = ItemListOfChartsBinding.bind(relativeLayout)
    }

    val data get() = _data

    fun setTitle(title: String) {
        binding.textChartTitle.text = title
        _data.title = title
    }
}