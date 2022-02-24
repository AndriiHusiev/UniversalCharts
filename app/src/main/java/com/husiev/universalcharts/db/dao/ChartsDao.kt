package com.husiev.universalcharts.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.husiev.universalcharts.db.entity.ChartsEntity

@Dao
interface ChartsDao {
    @Query("SELECT * FROM charts")
    fun loadChartsList(): LiveData<List<ChartsEntity>>

    @Query("SELECT * FROM charts where uid = :chartUid")
    fun loadChart(chartUid: String): LiveData<ChartsEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(chart: ChartsEntity)

    @Delete
    suspend fun delete(chart: ChartsEntity)

    @Update
    suspend fun update(chart: ChartsEntity)
}