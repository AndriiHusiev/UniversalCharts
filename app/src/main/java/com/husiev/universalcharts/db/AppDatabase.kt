package com.husiev.universalcharts.db

import android.content.Context
import android.graphics.Color
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.husiev.universalcharts.db.dao.ChartDataDao
import com.husiev.universalcharts.db.dao.ChartsDao
import com.husiev.universalcharts.db.dao.ColorsDao
import com.husiev.universalcharts.db.entity.ChartDataEntity
import com.husiev.universalcharts.db.entity.ChartsEntity
import com.husiev.universalcharts.db.entity.ColorsEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [ChartsEntity::class, ChartDataEntity::class, ColorsEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun chartsDao(): ChartsDao
    abstract fun chartsDataDao(): ChartDataDao
    abstract fun colorsDao(): ColorsDao

    private class AppDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val dao = database.colorsDao()
                    var color = ColorsEntity(1,"black", Color.BLACK)
                    dao.insert(color)
                    color = ColorsEntity(1,"blue", Color.BLUE)
                    dao.insert(color)
                    color = ColorsEntity(1,"green", Color.GREEN)
                    dao.insert(color)
                    color = ColorsEntity(1,"red", Color.RED)
                    dao.insert(color)
                    color = ColorsEntity(1,"gray", Color.GRAY)
                    dao.insert(color)
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "chart_database"
                )
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
