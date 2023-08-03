package com.husiev.universalcharts.di

import android.app.Application
import android.content.Context
import android.graphics.Color
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.husiev.universalcharts.UChartApplication
import com.husiev.universalcharts.db.AppDatabase
import com.husiev.universalcharts.db.dao.ColorsDao
import com.husiev.universalcharts.db.entity.ColorsEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesAppDatabase(
        @ApplicationContext context: Context,
        scope: CoroutineScope,
        provider: Provider<ColorsDao>
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "chart_database",
    )
        .addCallback(AppDatabaseCallback(scope, provider))
        .fallbackToDestructiveMigration()
        .addMigrations(MIGRATION_1_2)
        .build()

    @Provides
    @Singleton
    fun providesScope(
        application: Application
    ): CoroutineScope {
        return with(application as UChartApplication) {
            CoroutineScope(SupervisorJob())
        }
    }

    class AppDatabaseCallback(
        private val scope: CoroutineScope,
        private val provider: Provider<ColorsDao>
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            scope.launch {
                provider.get().insert(ColorsEntity("black", Color.BLACK))
                provider.get().insert(ColorsEntity("blue", Color.BLUE))
                provider.get().insert(ColorsEntity("green", Color.GREEN))
                provider.get().insert(ColorsEntity("red", Color.RED))
                provider.get().insert(ColorsEntity("gray", Color.GRAY))
            }
        }
    }

    @Singleton
    @Provides
    fun provideColorsDao(database: AppDatabase) = database.colorsDao()

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