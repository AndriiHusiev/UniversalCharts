package com.husiev.universalcharts.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.themeadapter.material.MdcTheme
import com.husiev.universalcharts.R

@Composable
fun TextFieldItem(
    value: String,
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    onChange: (String) -> Unit = {}
) {
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) ShowDialog(
        value = value,
        onConfirm = {
            showDialog = false
            onChange(it)
        },
        onDismiss = { showDialog = false }
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.padding_small))
            .clickable {
                showDialog = true
            },
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
        Text(
            text = value,
            modifier = Modifier
                .widthIn(1.dp, 200.dp),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}

@Composable
fun ShowDialog(
    value: String,
    modifier: Modifier = Modifier,
    onConfirm: (String) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    var userInput by remember { mutableStateOf(value) }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        title = {
            Text(text = stringResource(R.string.settings_dialog_title_text))
        },
        text = {
            TextField(
                value = userInput,
                onValueChange = { userInput = it },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(userInput) }) {
                Text(stringResource(R.string.dialog_confirm_text))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.dialog_dismiss_text))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun TextFieldItemPreview() {
    MdcTheme {
        TextFieldItem(
            value = "chart_preview",
            text = stringResource(R.string.settings_chart_label_text),
            icon = Icons.Filled.TextFields,
            onChange = {  }
        )
    }
}