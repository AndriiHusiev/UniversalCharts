package com.husiev.universalcharts.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "colors")
class ColorsEntity (
    @PrimaryKey(autoGenerate = true) val uid: Int,
    val title: String,
    val color: Int
)