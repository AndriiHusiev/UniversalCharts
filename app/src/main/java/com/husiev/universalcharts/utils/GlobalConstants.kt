package com.husiev.universalcharts.utils

import android.util.Log

const val CHART_INFO_FILENAME = "general_info"
const val FILE_EXTENSION_CSV = ".csv"
const val CSV_CELL_SEPARATOR: Char = ';'
const val NEW_LINE = "\n"
const val INTENT_CHART_ID = "chartID"
const val CHARTS_NUMBER = 5
const val COLOR_TEXT_DARK: Int = 0xFF51514B.toInt()
const val CHART_DATA_FILENAME = "data"

private const val APP_TAG = "UCharts: "
private const val dot = ". "
private const val colon = ": "
private const val SHOW_LOG = true
//private const val SHOW_LOG = false

fun logDebugOut(obj: String, message: String, param: String) {
    if (SHOW_LOG) {
        val compiledMessage = obj + dot + message + colon + param
        Log.d(APP_TAG, compiledMessage);
    }
}

fun logDebugOut(obj: String, message: String, param: Int) {
    if (SHOW_LOG) {
        val compiledMessage = obj + dot + message + colon + param
        Log.d(APP_TAG, compiledMessage);
    }
}

fun logDebugOut(obj: String, message: String, param: Float) {
    if (SHOW_LOG) {
        val compiledMessage = obj + dot + message + colon + param
        Log.d(APP_TAG, compiledMessage);
    }
}

fun logDebugOut(obj: String, message: String, param: Boolean) {
    if (SHOW_LOG) {
        val compiledMessage = obj + dot + message + colon + param
        Log.d(APP_TAG, compiledMessage);
    }
}