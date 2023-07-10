package com.husiev.universalcharts.db.entity

import androidx.room.ColumnInfo

data class SimpleChartData (
    @ColumnInfo(name = "chart_data_1") val chartData1: Float? = null,
    @ColumnInfo(name = "chart_data_2") val chartData2: Float? = null,
    @ColumnInfo(name = "chart_data_3") val chartData3: Float? = null,
    @ColumnInfo(name = "chart_data_4") val chartData4: Float? = null,
    @ColumnInfo(name = "chart_data_5") val chartData5: Float? = null
)