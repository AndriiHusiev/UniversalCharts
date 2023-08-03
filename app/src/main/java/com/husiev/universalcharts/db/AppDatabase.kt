package com.husiev.universalcharts.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.husiev.universalcharts.db.dao.ChartDataDao
import com.husiev.universalcharts.db.dao.ChartsDao
import com.husiev.universalcharts.db.dao.ColorsDao
import com.husiev.universalcharts.db.dao.SettingsDao
import com.husiev.universalcharts.db.entity.ChartDataEntity
import com.husiev.universalcharts.db.entity.ChartsEntity
import com.husiev.universalcharts.db.entity.ColorsEntity
import com.husiev.universalcharts.db.entity.SettingsEntity

@Database(
    entities = [
        ChartsEntity::class,
        ChartDataEntity::class,
        ColorsEntity::class,
        SettingsEntity::class,
    ],
    version = 2,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun chartsDao(): ChartsDao
    abstract fun chartsDataDao(): ChartDataDao
    abstract fun colorsDao(): ColorsDao
    abstract fun settingsDao(): SettingsDao

}
