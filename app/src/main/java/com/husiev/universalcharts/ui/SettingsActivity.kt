package com.husiev.universalcharts.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LineWeight
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.ViewArray
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.husiev.universalcharts.R
import com.husiev.universalcharts.ui.ui.theme.UniversalChartsTheme
import kotlin.math.roundToInt

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UniversalChartsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SettingsScreen()
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            SettingsTopAppBar(
                title = stringResource(R.string.title_activity_settings),
                canNavigateBack = true,
                modifier = modifier
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

@Composable
fun SettingsTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier
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
                IconButton(onClick = { }) {
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

@Composable
fun SwitchableItem(
    text: String,
    checked: Boolean,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    var checked by remember { mutableStateOf(true) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.padding_small)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon?: Icons.Default.Info,
            contentDescription = null
        )
        Text(
            text = text,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = dimensionResource(R.dimen.padding_medium))
        )
        Switch(
            checked = checked,
            onCheckedChange = {checked = it},
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Composable
fun SliderItem(
    value: Float,
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    val minValue = 0.2f
    val maxValue = 10f
    var sliderPosition by remember { mutableStateOf(value) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.padding_small)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon?: Icons.Default.Info,
            contentDescription = null
        )
        Column(
            modifier = modifier.padding(horizontal = dimensionResource(R.dimen.padding_small))
        ) {
            Text(
                text = "$text: $sliderPosition",
                modifier = Modifier
                    .padding(horizontal = dimensionResource(R.dimen.padding_small))
            )
            Slider(
                value = sliderPosition,
                onValueChange = { sliderPosition = (it * 10).roundToInt() / 10f },
                valueRange = minValue..maxValue,
                onValueChangeFinished = {
                    // launch some business logic update with the state you hold
                    // viewModel.updateSelectedSliderValue(sliderPosition)
                },
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer
                )
            )
        }
    }
}

@Composable
fun DropDownItem(
    text: String,
    modifier: Modifier = Modifier,
    selectedIndex: Int = 0,
    list: List<String> = listOf("Black", "White", "Blue", "Yellow"),
    icon: ImageVector? = null
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.padding_small)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon?: Icons.Default.Info,
            contentDescription = null
        )
        Text(
            text = text,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = dimensionResource(R.dimen.padding_small))
        )

        Box(
            modifier = Modifier
                .width(200.dp)
                .wrapContentSize(Alignment.TopStart),
        ) {
            Text(
                text = list[selectedIndex],
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { expanded = true })
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(size = 20.dp)
                    ),
                textAlign = TextAlign.Center
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                offset = DpOffset(40.dp, 0.dp)
            ) {
                list.forEachIndexed { index, label ->
                    androidx.compose.material3.DropdownMenuItem(
                        text = { Text(label) },
                        onClick = {
//                            selectedIndex = index
                            expanded = false
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Filled.ViewArray,
                                contentDescription = null,
                                tint = Color.Green
                            )
                        }
                    )
                }
            }
        }


    }
}

@Preview(showBackground = true, device = "spec:width=1080px,height=2340px,dpi=320")
@Composable
fun SettingsScreenPreview() {
    UniversalChartsTheme {
        SettingsScreen()
    }
}