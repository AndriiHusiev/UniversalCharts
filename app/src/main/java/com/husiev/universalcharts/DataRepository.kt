package com.husiev.universalcharts

import android.content.Context
import androidx.lifecycle.LiveData
import com.husiev.universalcharts.db.AppDatabase
import com.husiev.universalcharts.db.entity.ChartDataEntity
import com.husiev.universalcharts.db.entity.ChartsEntity
import com.husiev.universalcharts.db.entity.SettingsEntity
import com.husiev.universalcharts.db.entity.SimpleChartData
import com.husiev.universalcharts.utils.*
import com.husiev.universalcharts.utils.ExternalStorageOperations.Companion.createDirectory
import com.husiev.universalcharts.utils.ExternalStorageOperations.Companion.deleteDirectory
import com.husiev.universalcharts.utils.ExternalStorageOperations.Companion.saveDataToFile
import java.util.*
import kotlin.reflect.full.declaredMemberProperties

class DataRepository(context: Context, db: AppDatabase) {
    private val rootDirectory = context.getExternalFilesDir(null)
    private val database = db

    //<editor-fold desc="SelectionActivity">
    var listOfCharts: LiveData<List<ChartsEntity>> = database.chartsDao().loadChartsList()

    suspend fun insertChart(chartTitle: String) {
        val chartId = createChartOnFilesystem(chartTitle)
        val entity = ChartsEntity(chartId, chartTitle)

        database.chartsDao().insert(entity)

        repeat(CHARTS_NUMBER) {
            database.settingsDao().insert(
                SettingsEntity(
                    chartUid = chartId,
                    label = "chart_${it}",
                    color = it,
                    lineWidth = 2
                )
            )
        }
    }

    suspend fun deleteChart(id: String?) {
        id?.let {
            val entity = ChartsEntity(id)
            database.chartsDao().delete(entity)
            deleteChartOnFilesystem(id)
        }
    }
    //</editor-fold>

    //<editor-fold desc="ChartsActivity">
    fun loadChartWithData(id: String) = database.chartsDataDao().loadChartWithData(id)

    fun saveDataOnFilesystem(chartId: String, data: List<SimpleChartData>) {
        if (chartId != "") {
            val filename = getActualFilename(chartId)
            val dataCsv = convertDataToCsv(data)
            saveData(filename, dataCsv.toByteArray())
        }
    }
    //</editor-fold>

    //<editor-fold desc="EditActivity">
    val listOfColors = database.colorsDao().loadColorsList()

    fun loadListOfChartData(id: String) = database.chartsDataDao().loadData(id)

    suspend fun insertNewRowTo(chartId: String) {
        val data = SimpleChartData()
        val row = ChartDataEntity(chartId, data)
        database.chartsDataDao().insert(row)
    }

    suspend fun deleteLastRowOf(chartId: String, rowId: Int) {
        val row = ChartDataEntity(rowId, chartId, SimpleChartData())
        database.chartsDataDao().delete(row)
    }

    suspend fun updateRow(row: ChartDataEntity) {
        database.chartsDataDao().update(row)
    }
    //</editor-fold>

    //<editor-fold desc="Settings">
    fun getListOfKeys(chartId: String) = database.settingsDao().loadKeys(chartId)

    fun getListOfSettings(chartId: String, uid: Int) = database.settingsDao().loadSettings(chartId, uid)
    //</editor-fold>

    //<editor-fold desc="File Operations in Private Storage">
    private fun convertDataToCsv(data: List<SimpleChartData>): String {
        var dataCsv = ""
        val dots = SimpleChartData::class.declaredMemberProperties

        for (i in data.indices) {
            dataCsv += dots.joinToString(
                separator = CSV_CELL_SEPARATOR.toString(),
                postfix = CSV_CELL_SEPARATOR + NEW_LINE
            ) {
                "${it.get(data[i])?:""}"
            }
        }

        return dataCsv
    }

    private fun deleteChartOnFilesystem(id: String) {
        if (rootDirectory != null) {
            deleteDirectory(rootDirectory, id)
        }
    }

    private fun createChartOnFilesystem(chartTitle: String): String {
        // Actual timestamp is used as directory name
        val dirName = Date().time.toString()
        // Create directory with specified name
        createDirectory(dirName)
        val path = "$dirName/$CHART_INFO_FILENAME$FILE_EXTENSION_CSV"
        saveData(path, prepareDataToSaving(chartTitle).toByteArray())
        return dirName
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
    //</editor-fold>

    companion object {
        @Volatile
        private var INSTANCE: DataRepository? = null

        fun getInstance(context: Context, db: AppDatabase): DataRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = DataRepository(context, db)
                INSTANCE = instance
                instance
            }
        }

        private fun getActualFilename(chartId: String) = "$chartId/$CHART_DATA_FILENAME$FILE_EXTENSION_CSV"
    }
}