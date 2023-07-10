package com.husiev.universalcharts.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "charts")
data class ChartsEntity (
    @PrimaryKey val uid: String,
    var title: String
) {
    constructor(uid: String) : this(uid, "")
}