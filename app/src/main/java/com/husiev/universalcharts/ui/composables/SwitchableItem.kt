package com.husiev.universalcharts.ui.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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