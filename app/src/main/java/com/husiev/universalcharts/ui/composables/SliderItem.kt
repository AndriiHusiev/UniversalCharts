package com.husiev.universalcharts.ui.composables

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
import kotlin.math.roundToInt

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