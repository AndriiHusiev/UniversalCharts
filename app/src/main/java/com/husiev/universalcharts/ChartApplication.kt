package com.husiev.universalcharts

import android.app.Application

class ChartApplication: Application() {
    val repository by lazy { DataRepository.getInstance(this.applicationContext) }
}