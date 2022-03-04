package com.husiev.universalcharts.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.husiev.universalcharts.db.entity.ChartDataEntity
import com.husiev.universalcharts.db.entity.ChartWithData

@Dao
interface ChartDataDao {
    @Transaction
    @Query("SELECT * FROM charts WHERE uid = :chartUid")
    fun loadChartWithData(chartUid: String): LiveData<ChartWithData>

    @Query("SELECT * FROM chart_data WHERE chart_uid = :chartUid")
    fun loadData(chartUid: String): LiveData<List<ChartDataEntity>>

    @Query("SELECT * FROM chart_data")
    fun loadAllData(): LiveData<List<ChartDataEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(chart: ChartDataEntity)

    @Delete
    suspend fun delete(chart: ChartDataEntity)

    @Update
    suspend fun update(chart: ChartDataEntity)
}