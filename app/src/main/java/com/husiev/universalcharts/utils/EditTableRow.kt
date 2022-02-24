package com.husiev.universalcharts.utils

import android.content.Context
import android.graphics.Point
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.husiev.universalcharts.R
import com.husiev.universalcharts.databinding.ItemEditTableRowBinding

class EditTableRow(context: Context) : LinearLayout(context) {
    private var binding: ItemEditTableRowBinding

    init {
        val mInflater = LayoutInflater.from(context)
        val linearLayout = mInflater.inflate(
            R.layout.item_edit_table_row,
            this,
            true
        ) as LinearLayout
        binding = ItemEditTableRowBinding.bind(linearLayout)
    }

    var rowIndex: Int = 0
        set(value) {
            val x = value + 1
            binding.textChartPointNumber.text = x.toString()
            field = value
        }

    fun setCell(index: Int, text: String) {
        when(index) {
            0 -> {
                binding.textChart01Point.text = text
                binding.textChart01Point.tag = "$rowIndex$index"
            }
            1 -> {
                binding.textChart02Point.text = text
                binding.textChart02Point.tag = "$rowIndex$index"
            }
            2 -> {
                binding.textChart03Point.text = text
                binding.textChart03Point.tag = "$rowIndex$index"
            }
            3 -> {
                binding.textChart04Point.text = text
                binding.textChart04Point.tag = "$rowIndex$index"
            }
            4 -> {
                binding.textChart05Point.text = text
                binding.textChart05Point.tag = "$rowIndex$index"
            }
        }
    }

    fun setCellClickListener(listener: OnClickListener, index: Int) {
        when(index) {
            0 -> binding.textChart01Point.setOnClickListener(listener)
            1 -> binding.textChart02Point.setOnClickListener(listener)
            2 -> binding.textChart03Point.setOnClickListener(listener)
            3 -> binding.textChart04Point.setOnClickListener(listener)
            4 -> binding.textChart05Point.setOnClickListener(listener)
        }
    }

    fun setCellLongClickListener(listener: OnLongClickListener, index: Int) {
        when(index) {
            0 -> binding.textChart01Point.setOnLongClickListener(listener)
            1 -> binding.textChart02Point.setOnLongClickListener(listener)
            2 -> binding.textChart03Point.setOnLongClickListener(listener)
            3 -> binding.textChart04Point.setOnLongClickListener(listener)
            4 -> binding.textChart05Point.setOnLongClickListener(listener)
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