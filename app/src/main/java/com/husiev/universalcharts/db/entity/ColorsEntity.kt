package com.husiev.universalcharts.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "colors")
data class ColorsEntity (
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val title: String,
    val color: Int
) {
    constructor(title: String, color: Int): this(0, title, color)
}