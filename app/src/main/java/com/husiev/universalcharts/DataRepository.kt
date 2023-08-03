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
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.full.declaredMemberProperties

@Singleton
class DataRepository @Inject constructor(
    @ApplicationContext context: Context,
    private val database: AppDatabase
) {
    private val rootDirectory = context.getExternalFilesDir(null)

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
    fun getListOfSettings(chartId: String) = database.settingsDao().loadSettings(chartId)

    fun updateField(
        uid: Int,
        label: String?,
        isVisible: Boolean?,
        showDots: Boolean?,
        curved: Boolean?,
        color: Int?,
        lineWidth: Int?
    ) {
        label?.let { database.settingsDao().updateLabel(it, uid) }
        isVisible?.let { database.settingsDao().updateVisibility(it, uid) }
        showDots?.let { database.settingsDao().updateDots(it, uid) }
        curved?.let { database.settingsDao().updateCurved(it, uid) }
        color?.let { database.settingsDao().updateColor(it, uid) }
        lineWidth?.let { database.settingsDao().updateLineWidth(it, uid) }
    }
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
}