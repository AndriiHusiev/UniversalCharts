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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.themeadapter.material.MdcTheme
import com.husiev.universalcharts.R

@Composable
fun SettingsTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    onClose: () -> Unit = {}
) {
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
}
@Composable
fun SettingsBody(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(horizontal = dimensionResource(R.dimen.padding_small))
    ) {
        SwitchableItem(
            text = stringResource(R.string.settings_show_chart_text),
            checked = true,
            icon = Icons.Filled.Visibility
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.padding_medium)))
        SwitchableItem(
            text = stringResource(R.string.settings_show_dots_text),
            checked = true,
            icon = Icons.Filled.Timeline
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.padding_medium)))
        SwitchableItem(
            text = stringResource(R.string.settings_curved_text),
            checked = true,
            icon = Icons.Filled.TrendingUp
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.padding_medium)))
        SliderItem(
            value = 2f,
            text = stringResource(R.string.settings_chart_line_width_text),
            icon = Icons.Filled.LineWeight
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.padding_medium)))
        DropDownItem(
            text = stringResource(R.string.settings_chart_line_color_text),
            icon = Icons.Filled.Opacity
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    MdcTheme {
        SettingsBody()
    }
}