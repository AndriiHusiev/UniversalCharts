package com.husiev.universalcharts.db.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ChartWithData (
    @Embedded val chart: ChartsEntity,
    @Relation(
        parentColumn = "uid",
        entityColumn = "chart_uid"
    )
    val data: List<ChartDataEntity>
)