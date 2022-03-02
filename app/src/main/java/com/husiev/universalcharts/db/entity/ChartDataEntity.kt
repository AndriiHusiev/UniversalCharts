package com.husiev.universalcharts.db.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chart_data")
class ChartDataEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "chart_uid") val chartUid: String,
    @Embedded val data: SimpleChartData?
) {
    constructor(chartUid: String, data: SimpleChartData) : this(null, chartUid, data)
}