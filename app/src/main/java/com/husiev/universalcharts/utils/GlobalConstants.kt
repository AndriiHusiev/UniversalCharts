package com.husiev.universalcharts.utils

import android.graphics.Color
import android.util.Log

const val CHART_INFO_FILENAME = "general_info"
const val FILE_EXTENSION_CSV = ".csv"
const val CSV_CELL_SEPARATOR: Char = ';'
const val NEW_LINE = "\n"
const val INTENT_CHART_ID = "chartID"
const val CHARTS_NUMBER = 5
const val COLOR_TEXT_DARK: Int = 0xFF51514B.toInt()
const val CHART_DATA_FILENAME = "data"
val colors = listOf(Color.BLACK, Color.BLUE, Color.GREEN, Color.RED, Color.GRAY)

private const val APP_TAG = "UCharts: "
private const val SHOW_LOG = true
//private const val SHOW_LOG = false

fun logDebugOut(obj: String, message: String, param: Any) {
    if (SHOW_LOG) {
        val compiledMessage = "$obj. $message: $param"
        Log.d(APP_TAG, compiledMessage)
    }
}

fun getActualFilename(chartId: String) = "$chartId/$CHART_DATA_FILENAME$FILE_EXTENSION_CSV"