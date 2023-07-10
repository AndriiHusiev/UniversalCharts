package com.husiev.universalcharts.db

import android.content.Context
import android.graphics.Color
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.husiev.universalcharts.db.dao.ChartDataDao
import com.husiev.universalcharts.db.dao.ChartsDao
import com.husiev.universalcharts.db.dao.ColorsDao
import com.husiev.universalcharts.db.dao.SettingsDao
import com.husiev.universalcharts.db.entity.ChartDataEntity
import com.husiev.universalcharts.db.entity.ChartsEntity
import com.husiev.universalcharts.db.entity.ColorsEntity
import com.husiev.universalcharts.db.entity.SettingsEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [ChartsEntity::class, ChartDataEntity::class, ColorsEntity::class, SettingsEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun chartsDao(): ChartsDao
    abstract fun chartsDataDao(): ChartDataDao
    abstract fun colorsDao(): ColorsDao
    abstract fun settingsDao(): SettingsDao

    private class AppDatabaseCallback(private val scope: CoroutineScope) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val dao = database.colorsDao()
                    var color = ColorsEntity("black", Color.BLACK)
                    dao.insert(color)
                    color = ColorsEntity("blue", Color.BLUE)
                    dao.insert(color)
                    color = ColorsEntity("green", Color.GREEN)
                    dao.insert(color)
                    color = ColorsEntity("red", Color.RED)
                    dao.insert(color)
                    color = ColorsEntity("gray", Color.GRAY)
                    dao.insert(color)
                }
            }
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
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
                    .fallbackToDestructiveMigration()
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `settings` (" +
                        "`uid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "`chart_uid` TEXT NOT NULL, `label` TEXT NOT NULL, `visible` INTEGER NOT NULL, " +
                        "`dots` INTEGER NOT NULL, `curved` INTEGER NOT NULL, `color` INTEGER NOT NULL, " +
                        "`width` INTEGER NOT NULL, FOREIGN KEY(`chart_uid`) REFERENCES `charts`(`uid`) " +
                        "ON UPDATE NO ACTION ON DELETE CASCADE )")
                database.execSQL("CREATE INDEX IF NOT EXISTS `index_settings_chart_uid` ON `settings` (`chart_uid`)")
            }
        }
    }
}
