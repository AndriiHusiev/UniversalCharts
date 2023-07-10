package com.husiev.universalcharts.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.husiev.universalcharts.db.entity.SettingsEntity

@Dao
interface SettingsDao: BaseDao<SettingsEntity> {
    @Query("SELECT * FROM settings where chart_uid = :chartUid")
    fun loadSettings(chartUid: String): LiveData<SettingsEntity?>
}