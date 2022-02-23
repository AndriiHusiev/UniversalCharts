package com.husiev.universalcharts

import android.content.Context
import com.husiev.universalcharts.utils.*
import com.husiev.universalcharts.utils.ExternalStorageOperations.Companion.createDirectory
import com.husiev.universalcharts.utils.ExternalStorageOperations.Companion.deleteDirectory
import com.husiev.universalcharts.utils.ExternalStorageOperations.Companion.getListOfDirs
import com.husiev.universalcharts.utils.ExternalStorageOperations.Companion.readLinesFromFile
import com.husiev.universalcharts.utils.ExternalStorageOperations.Companion.saveDataToFile
import java.util.*

class DataRepository(context: Context) {
    private val rootDirectory = context.getExternalFilesDir(null)

    var listOfDirectories = rootDirectory?.let { getListOfDirs(it, "") }

    fun saveChartData(chartId: String?, data: Array<Array<String>>): Boolean {
        if (chartId != null) {
            val filename = getActualFilename(chartId)
            var dataCsv = ""
            for (i in data.indices) {
                for (j in data[i].indices)
                    dataCsv += "${data[i][j]}$CSV_CELL_SEPARATOR"
                dataCsv += NEW_LINE
            }
            return saveData(filename, dataCsv.toByteArray())
        }
        return false
    }

    fun getChartTitle(chartId: String?): String {
        var title = ""
        try {
            if (chartId != null) {
                val filename = "$chartId/$CHART_INFO_FILENAME$FILE_EXTENSION_CSV"
                val data = getLinesFromFile(filename)
                if (data != null && data.size > 1)
                    title = data[1].substring(0, data[1].length-1)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return title
    }

    fun createNewChart(chartTitle: String): String {
        // Actual timestamp is used as directory name
        val dirName = Date().time.toString()
        // Create directory with specified name
        createDirectory(dirName)
        val path = "$dirName/$CHART_INFO_FILENAME$FILE_EXTENSION_CSV"
        saveData(path, prepareDataToSaving(chartTitle).toByteArray())
        return dirName
    }

    fun deleteChart(id: String?) {
        id?.let {
            if (rootDirectory != null) {
                deleteDirectory(rootDirectory, id)
            }
        }
    }

    fun getChartData(chartId: String?): Array<Array<String>> {
        if (chartId == null)
            return emptyArray()
        val filename = getActualFilename(chartId)
        val lines = getLinesFromFile(filename)
        return convertCsvToStringMatrix(lines)
    }

    private fun getLinesFromFile(filename: String): List<String>? {
        return if (rootDirectory != null)
            readLinesFromFile(rootDirectory, filename)
        else
            null
    }

    private fun saveData(filename: String, data: ByteArray, append: Boolean = false): Boolean {
        if (rootDirectory != null) {
            return saveDataToFile(rootDirectory, filename, data, append)
        }
        return false
    }

    private fun createDirectory(pathname: String) {
        if (rootDirectory != null) {
            createDirectory(rootDirectory, pathname)
        }
    }

    private fun prepareDataToSaving(chartName: String): String {
        val data: StringBuilder = StringBuilder()
        data.append("Title").append(CSV_CELL_SEPARATOR).append(NEW_LINE)
            .append(chartName).append(CSV_CELL_SEPARATOR).append(NEW_LINE)
        return data.toString()
    }

    private fun convertCsvToStringMatrix(data: List<String>?): Array<Array<String>> {
        if (data == null) return emptyArray()

        var chartData = arrayOf<Array<String>>()

        try {
            data.forEach { line ->
                var cell = ""
                var cells = arrayOf<String>()

                for (i in line.indices) {
                    if (line[i] == CSV_CELL_SEPARATOR) {
                        cells += cell
                        cell = ""
                    }
                    else {
                        cell += line[i]
                    }
                }
                chartData += cells
            }
            return chartData
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            return emptyArray()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: DataRepository? = null

        fun getInstance(context: Context): DataRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = DataRepository(context)
                INSTANCE = instance
                instance
            }
        }

        private fun getActualFilename(chartId: String) = "$chartId/$CHART_DATA_FILENAME$FILE_EXTENSION_CSV"
    }
}