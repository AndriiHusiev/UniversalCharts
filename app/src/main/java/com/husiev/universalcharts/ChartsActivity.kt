package com.husiev.universalcharts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.husiev.universalcharts.databinding.ActivityChartsBinding

class ChartsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChartsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChartsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
    }
}