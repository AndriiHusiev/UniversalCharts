package com.husiev.universalcharts.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LineWeight
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.themeadapter.material.MdcTheme
import com.husiev.universalcharts.R
import com.husiev.universalcharts.db.entity.SettingsEntity

@Composable
fun SettingsTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    onClose: () -> Unit = {},
    tabTitles: List<String>? = null,
    tabSelected: Int = 0,
    onTabSelected: (Int) -> Unit = {}
) {
    Column {
        TopAppBar(
            title = { Text(
                text = title,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleLarge
            ) },
            modifier = modifier,
            navigationIcon = {
                if (canNavigateBack) {
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        )

        tabTitles?.let {
            TabRow(selectedTabIndex = tabSelected) {
                it.forEachIndexed { index, title ->
                    Tab(
                        selected = tabSelected == index,
                        onClick = { onTabSelected(index) },
                        text = { Text(text = title) }
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsBody(
    content: SettingsEntity?,
    modifier: Modifier = Modifier,
    listColors: List<String> = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5"),
    onChange: (Any, String) -> Unit = { _, _ -> }
) {
    Column(
        modifier = modifier
            .padding(
                horizontal = dimensionResource(R.dimen.padding_small),
                vertical = dimensionResource(R.dimen.padding_medium)
            )
    ) {
        content?.let {
            SwitchableItem(
                text = stringResource(R.string.settings_show_chart_text),
                checked = content.isVisible,
                icon = Icons.Filled.Visibility,
                onSwitch = { onChange(it, "visible") }
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_medium)))
            SwitchableItem(
                text = stringResource(R.string.settings_show_dots_text),
                checked = content.showDots,
                icon = Icons.Filled.Timeline,
                onSwitch = { onChange(it, "dots") }
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_medium)))
            SwitchableItem(
                text = stringResource(R.string.settings_curved_text),
                checked = content.curved,
                icon = Icons.Filled.TrendingUp,
                onSwitch = { onChange(it, "curved") }
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_medium)))
            SliderItem(
                value = content.lineWidth.toFloat(),
                text = stringResource(R.string.settings_chart_line_width_text),
                icon = Icons.Filled.LineWeight
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_medium)))
            DropDownItem(
                text = stringResource(R.string.settings_chart_line_color_text),
                icon = Icons.Filled.Opacity,
                selectedIndex = content.color,
                list = listColors,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsAppBarPreview() {
    MdcTheme {
        SettingsTopAppBar(
            title = "Settings",
            canNavigateBack = true,
            tabTitles = listOf("Tab 1", "Tab 2", "Tab 3", "Tab 4", "Tab 5")
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    MdcTheme {
        SettingsBody(
            SettingsEntity(
                chartUid = "",
                label = "chart_preview",
                color = 1,
                lineWidth = 2
            )
        )
    }
}