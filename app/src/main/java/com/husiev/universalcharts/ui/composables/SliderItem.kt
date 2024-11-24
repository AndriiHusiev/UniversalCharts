package com.husiev.universalcharts.ui.composables

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
    var sliderPosition by remember { mutableFloatStateOf(value.toFloat()) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.padding_small))
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon?: Icons.Default.Info,
            contentDescription = null
        )
        Column(
            modifier = Modifier
                .padding(horizontal = dimensionResource(R.dimen.padding_small))
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Crossfade(
                    targetState = expanded,
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(R.dimen.padding_small))
                        .weight(1f),
                    label = "AnimSliderLabel"
                ) {
                    when (it) {
                        true -> Text(text = "$text: ${sliderPosition.toInt()}")
                        false -> Text(text = text)
                    }
                }
                IconButton(onClick = onClick) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = if (expanded) "Show less" else "Show more"
                    )
                }

            }
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