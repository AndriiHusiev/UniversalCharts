package com.husiev.universalcharts

import android.app.Application

class UChartApplication: Application() {
    val repository by lazy { DataRepository.getInstance(this.applicationContext) }
}