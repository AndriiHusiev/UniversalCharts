package com.husiev.universalcharts

import android.content.Context
import com.husiev.universalcharts.utils.CHART_INFO_FILENAME
import com.husiev.universalcharts.utils.ExternalStorageOperations.Companion.createDirectory
import com.husiev.universalcharts.utils.ExternalStorageOperations.Companion.getListOfDirs
import com.husiev.universalcharts.utils.ExternalStorageOperations.Companion.readLinesFromFile
import com.husiev.universalcharts.utils.ExternalStorageOperations.Companion.saveDataToFile
import com.husiev.universalcharts.utils.FILE_EXTENSION_CSV

class DataRepository(context: Context) {
    private val rootDirectory = context.getExternalFilesDir(null)

    var listOfDirectories = rootDirectory?.let { getListOfDirs(it, "") }

    fun createDirectory(pathname: String) {
        if (rootDirectory != null) {
            createDirectory(rootDirectory, pathname)
        }
    }

    fun saveDataToFile(filename: String, data: ByteArray?, append: Boolean) {
        if (rootDirectory != null) {
            saveDataToFile(rootDirectory, filename, data, append)
        }
    }

    fun getLinesFromFile(filename: String): List<String>? {
        return if (rootDirectory != null)
            readLinesFromFile(rootDirectory, filename)
        else
            null
    }

    fun getChartTitle(id: String?): String {
        return try {
            val filename = "$id/$CHART_INFO_FILENAME$FILE_EXTENSION_CSV"
            val data = getLinesFromFile(filename)
            if (data != null && data.size > 1)
                data[1].substring(0, data[1].length-1)
            else
                ""
        } catch (e: Exception) {
            e.printStackTrace()
            ""
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
    }
}