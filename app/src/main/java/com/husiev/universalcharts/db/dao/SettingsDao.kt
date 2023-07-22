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

    @Query("UPDATE settings SET label = :text WHERE uid = :uid")
    fun updateLabel(text: String, uid: Int)

    @Query("UPDATE settings SET visible = :isVisible WHERE uid = :uid")
    fun updateVisibility(isVisible: Boolean, uid: Int)

    @Query("UPDATE settings SET dots = :showDots WHERE uid = :uid")
    fun updateDots(showDots: Boolean, uid: Int)

    @Query("UPDATE settings SET curved = :curved WHERE uid = :uid")
    fun updateCurved(curved: Boolean, uid: Int)

    @Query("UPDATE settings SET color = :color WHERE uid = :uid")
    fun updateColor(color: Int, uid: Int)

    @Query("UPDATE settings SET width = :lineWidth WHERE uid = :uid")
    fun updateLineWidth(lineWidth: Int, uid: Int)
}