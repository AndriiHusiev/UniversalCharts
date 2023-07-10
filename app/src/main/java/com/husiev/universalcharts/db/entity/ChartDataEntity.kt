package com.husiev.universalcharts.db.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "chart_data",
    foreignKeys = [ForeignKey(
        entity = ChartsEntity::class,
        parentColumns = arrayOf("uid"),
        childColumns = arrayOf("chart_uid"),
        onDelete = CASCADE
    )],
    indices = [Index(value = arrayOf("chart_uid"))]
)
data class ChartDataEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "chart_uid") val chartUid: String,
    @Embedded val data: SimpleChartData?
) {
    constructor(chartUid: String, data: SimpleChartData) : this(0, chartUid, data)
}