package com.husiev.universalcharts.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.husiev.universalcharts.db.entity.SettingsEntity
import com.husiev.universalcharts.db.entity.SettingsKey

@Dao
interface SettingsDao: BaseDao<SettingsEntity> {
    @Query("SELECT uid, chart_uid FROM settings where chart_uid = :chartUid")
    fun loadKeys(chartUid: String): LiveData<List<SettingsKey>>

    @Query("SELECT * FROM settings where chart_uid = :chartUid AND uid = :uid")
    fun loadSettings(chartUid: String, uid: Int): LiveData<SettingsEntity?>
}