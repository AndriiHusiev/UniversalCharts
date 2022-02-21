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

    fun saveData(filename: String?, data: ByteArray?, append: Boolean) {
        if (rootDirectory != null && filename != null) {
            saveDataToFile(rootDirectory, filename, data, append)
        }
    }

    fun getLinesFromFile(filename: String): List<String>? {
        return if (rootDirectory != null)
            readLinesFromFile(rootDirectory, filename)
        else
            null
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
        saveData(path, prepareDataToSaving(chartTitle).toByteArray(), false)
        return dirName
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

    fun deleteChart(id: String?) {
        id?.let {
            if (rootDirectory != null) {
                deleteDirectory(rootDirectory, id)
            }
        }
    }

    fun getChartData(chartId: String): List<String>? {
        return getLinesFromFile(getActualFilename(chartId))
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

        fun getActualFilename(chartId: String) = "$chartId/$CHART_DATA_FILENAME$FILE_EXTENSION_CSV"
    }
}