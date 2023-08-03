package com.husiev.universalcharts.ui.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
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
import com.husiev.universalcharts.utils.*
import com.husiev.universalcharts.viewmodels.SettingsViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel,
    onClose: () -> Unit = {}
) {
    var curTab by rememberSaveable { mutableStateOf(0) }
    val curChartSettings by settingsViewModel.getSettings().observeAsState()
    val colors by settingsViewModel.allColors.observeAsState()

    Scaffold(
        topBar = {
            SettingsTopAppBar(
                title = stringResource(R.string.title_activity_settings),
                canNavigateBack = true,
                modifier = modifier,
                onClose = onClose,
                tabTitles = listOf("Chart 1", "Chart 2", "Chart 3", "Chart 4", "Chart 5"),
                tabSelected = curTab,
                onTabSelected = { curTab = it }
            )
        }
    ) { innerPadding ->
        curChartSettings?.run {
            AnimatedContent(
                targetState = curTab, label = "AnimTabContent",
            ) {
                SettingsBody(
                    content = this@run[curTab],
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxWidth(),
                    listColors = colors
                ) { value, tag ->
                    this@run[curTab]?.let {
                        when (tag) {
                            "label" -> settingsViewModel.updateField(it.uid, label = value as String)
                            "visible" -> settingsViewModel.updateField(it.uid, isVisible = value as Boolean)
                            "dots" -> settingsViewModel.updateField(it.uid, showDots = value as Boolean)
                            "curved" -> settingsViewModel.updateField(it.uid, curved = value as Boolean)
                            "width" -> settingsViewModel.updateField(it.uid, lineWidth = value as Int)
                            "color" -> settingsViewModel.updateField(it.uid, color = value as Int)
                        }
                    }
                }
            }
        }
    }
}