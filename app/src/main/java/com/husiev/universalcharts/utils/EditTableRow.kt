package com.husiev.universalcharts.utils

import android.content.Context
import android.graphics.Point
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
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

    fun getCell(index: Int): TextView? {
        return when(index) {
            0 -> binding.textChart01Point
            1 -> binding.textChart02Point
            2 -> binding.textChart03Point
            3 -> binding.textChart04Point
            4 -> binding.textChart05Point
            else -> null
        }
    }
}

fun tagOfEditTableCell(rowIndex: Int, cellIndex: Int) = "$rowIndex$cellIndex"

fun getPointPosition(tag: String): Point {
    return Point().apply {
        x = tag[tag.length-1].digitToInt()
        y = tag.substring(0, tag.length-2).toInt()
    }
}