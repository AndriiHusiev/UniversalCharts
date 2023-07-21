package com.husiev.universalcharts.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "settings",
    foreignKeys = [ForeignKey(
        entity = ChartsEntity::class,
        parentColumns = arrayOf("uid"),
        childColumns = arrayOf("chart_uid"),
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = arrayOf("chart_uid"))]
)
data class SettingsEntity (
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    @ColumnInfo(name = "chart_uid")
    val chartUid: String,
    val label: String,
    @ColumnInfo(name = "visible")
    val isVisible: Boolean = true,
    @ColumnInfo(name = "dots")
    val showDots: Boolean = true,
    val curved: Boolean = true,
    val color: Int,
    @ColumnInfo(name = "width")
    val lineWidth: Int,
)

data class SettingsKey(
    @ColumnInfo(name = "uid") val uid: Int,
    @ColumnInfo(name = "chart_uid") val chartUid: String
)