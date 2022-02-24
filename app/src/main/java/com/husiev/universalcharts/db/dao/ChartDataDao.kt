package com.husiev.universalcharts.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.husiev.universalcharts.db.entity.ChartDataEntity
import com.husiev.universalcharts.db.entity.SimpleChartData

@Dao
interface ChartDataDao {
    @Query("SELECT * FROM chart_data where chart_uid = :chartUid")
    fun loadData(chartUid: Int): LiveData<List<SimpleChartData?>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(chart: ChartDataEntity)

    @Delete
    suspend fun delete(chart: ChartDataEntity)

    @Update
    suspend fun update(chart: ChartDataEntity)
}