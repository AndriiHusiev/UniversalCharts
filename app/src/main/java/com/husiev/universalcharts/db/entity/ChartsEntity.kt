package com.husiev.universalcharts.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "charts")
class ChartsEntity (
    @PrimaryKey val uid: Int,
    val title: String
)