package com.husiev.universalcharts.ui.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.husiev.universalcharts.R
import com.husiev.universalcharts.utils.logDebugOut
import com.husiev.universalcharts.viewmodels.SettingsViewModel

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel,
    onClose: () -> Unit = {}
) {
    val key by settingsViewModel.keys.observeAsState()
    val curChartSettings by settingsViewModel.getSettingsOfChart(key?.let{it[0].uid}?:0).observeAsState()
    logDebugOut("SettingsScreen", "curChartSettings", key.isNullOrEmpty())

    Scaffold(
        topBar = {
            SettingsTopAppBar(
                title = stringResource(R.string.title_activity_settings),
                canNavigateBack = true,
                modifier = modifier,
                onClose = onClose
            )
        }
    ) { innerPadding ->
        SettingsBody(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        )
    }
}