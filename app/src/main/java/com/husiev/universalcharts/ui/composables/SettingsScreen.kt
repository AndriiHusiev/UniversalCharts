package com.husiev.universalcharts.ui.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.husiev.universalcharts.R
import com.husiev.universalcharts.viewmodels.SettingsViewModel

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel,
    onClose: () -> Unit = {}
) {
    val key by settingsViewModel.keys.observeAsState()
    var curTab by rememberSaveable { mutableStateOf(0) }
    var curKey = key?.let{it[curTab].uid}?:0
    val curChartSettings by settingsViewModel.getSettingsOfChart(curKey).observeAsState()
    val colors by settingsViewModel.allColors.observeAsState()

    Scaffold(
        topBar = {
            SettingsTopAppBar(
                title = stringResource(R.string.title_activity_settings),
                canNavigateBack = true,
                modifier = modifier,
                onClose = onClose,
                tabTitles = null,
                tabSelected = curTab,
                onTabSelected = { curTab = it }
            )
        }
    ) { innerPadding ->
        SettingsBody(
            content = curChartSettings,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
            onChange = {value, tag ->
                when(tag) {
                    "label" -> settingsViewModel.updateField(curKey, label = value as String)
                    "visible" -> settingsViewModel.updateField(curKey, isVisible = value as Boolean)
                    "dots" -> settingsViewModel.updateField(curKey, showDots = value as Boolean)
                    "curved" -> settingsViewModel.updateField(curKey, curved = value as Boolean)
                    "width" -> settingsViewModel.updateField(curKey, lineWidth = value as Int)
                    "color" -> settingsViewModel.updateField(curKey, color = value as Int)
                }
            }
        )
    }
}