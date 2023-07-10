package com.husiev.universalcharts.utils

import android.content.Context
import android.graphics.Point
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.husiev.universalcharts.R
import com.husiev.universalcharts.databinding.ItemEditTableRowBinding
import com.husiev.universalcharts.db.entity.ChartDataEntity
import com.husiev.universalcharts.db.entity.SimpleChartData
import kotlin.reflect.full.declaredMemberProperties

class EditTableRow(context: Context) : LinearLayout(context) {
    constructor(context: Context, dataEntity: ChartDataEntity): this(context) {
        rowId = dataEntity.id

        dataEntity.data?.let {
            for ((i, property) in SimpleChartData::class.declaredMemberProperties.withIndex()) {
                setCell(i, convert(property.get(it) as Float?))
            }
        }
    }

    private val points: MutableList<TextView> = mutableListOf()
    private var binding: ItemEditTableRowBinding
    var rowId: Int = 0

    init {
        val mInflater = LayoutInflater.from(context)
        val linearLayout = mInflater.inflate(
            R.layout.item_edit_table_row,
            this,
            true
        ) as LinearLayout
        binding = ItemEditTableRowBinding.bind(linearLayout)

        repeat(CHARTS_NUMBER) {index ->
            points.add(findViewWithTag("chart${index+1}"))
        }
    }

    var rowIndex: Int = 0
        set(value) {
            val x = value + 1
            binding.textChartPointNumber.text = x.toString()
            field = value
        }

    fun setCell(index: Int, text: String) {
        if (index in points.indices) {
            points[index].text = text
            points[index].tag = "$rowIndex$index"
        }
    }

    fun getCell(index: Int) =
        if (index in points.indices)
            points[index].text.toString()
        else
            ""

    fun toChartDataEntity(chartUid: String) = ChartDataEntity(
        id = rowId,
        chartUid = chartUid,
        data = SimpleChartData(
            getCell(0).toFloatOrNull(),
            getCell(1).toFloatOrNull(),
            getCell(2).toFloatOrNull(),
            getCell(3).toFloatOrNull(),
            getCell(4).toFloatOrNull(),
        )
    )

    fun setCellClickListener(listener: OnClickListener, index: Int) {
        points[index].setOnClickListener(listener)
    }

    fun setCellLongClickListener(listener: OnLongClickListener, index: Int) {
        points[index].setOnLongClickListener(listener)
    }

    companion object {
        fun convert(f: Float?): String {
            return f?.toString() ?: ""
        }
    }
}

fun tagOfEditTableCell(rowIndex: Int, cellIndex: Int) = "$rowIndex$cellIndex"

fun getPointPosition(tag: String): Point {
    return Point().apply {
        x = tag[tag.length-1].digitToInt()
        y = tag.substring(0, tag.length-1).toInt()
    }
}