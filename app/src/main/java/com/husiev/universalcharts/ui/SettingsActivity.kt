package com.husiev.universalcharts.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import com.google.accompanist.themeadapter.material.MdcTheme
import com.husiev.universalcharts.ui.composables.SettingsScreen
import com.husiev.universalcharts.utils.INTENT_CHART_ID
import com.husiev.universalcharts.viewmodels.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntentData()

        setContentView(
            ComposeView(this).apply {
                setContent {
                    MdcTheme {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background,
                        ) {
                            SettingsScreen(
                                settingsViewModel = settingsViewModel,
                                onClose = { finish() }
                            )
                        }
                    }
                }
            }
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun getIntentData() {
        intent.getStringExtra(INTENT_CHART_ID)?.let {
            settingsViewModel.chartId = it
        }
    }
}