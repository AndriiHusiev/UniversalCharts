package com.husiev.universalcharts.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.ViewModelProvider
import com.google.accompanist.themeadapter.material.MdcTheme
import com.husiev.universalcharts.ui.composables.SettingsScreen
import com.husiev.universalcharts.utils.INTENT_CHART_ID
import com.husiev.universalcharts.viewmodels.SettingsModelFactory
import com.husiev.universalcharts.viewmodels.SettingsViewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntentData()

        setContentView(
            ComposeView(this).apply {
                consumeWindowInsets = false
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
            settingsViewModel = ViewModelProvider(
                owner = this,
                factory = SettingsModelFactory(
                    chartId = it,
                    application = application
                )
            )[SettingsViewModel::class.java]
        }
    }
}