package com.husiev.universalcharts

import android.app.Application
import com.husiev.universalcharts.db.AppDatabase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class UChartApplication: Application() {
//    val applicationScope = CoroutineScope(SupervisorJob())

//    private val database by lazy { AppDatabase.getInstance(this, applicationScope) }
//    val repository by lazy { DataRepository.getInstance(this.applicationContext, database) }
}