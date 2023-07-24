package com.husiev.universalcharts.ui.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import com.husiev.universalcharts.R

@Composable
fun SliderItem(
    value: Int,
    text: String,
    expanded: Boolean,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    onChange: (Int) -> Unit = {},
    onClick: () -> Unit = {}
) {
    val minValue = 1f
    val maxValue = 10f
    var sliderPosition by remember { mutableStateOf(value.toFloat()) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(R.dimen.padding_small),
                vertical = dimensionResource(R.dimen.padding_semi_medium)
            )
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon?: Icons.Default.Info,
            contentDescription = null
        )
        Column(
            modifier = modifier
                .padding(horizontal = dimensionResource(R.dimen.padding_small))
                .animateContentSize()
        ) {
            Text(
                text = "$text: ${sliderPosition.toInt()}",
                modifier = Modifier
                    .padding(horizontal = dimensionResource(R.dimen.padding_small))
            )
            if (expanded) {
                Slider(
                    value = sliderPosition,
                    onValueChange = { sliderPosition = it },
                    valueRange = minValue..maxValue,
                    steps = (maxValue - minValue - 1).toInt(),
                    onValueChangeFinished = {
                        onChange(sliderPosition.toInt())
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
}