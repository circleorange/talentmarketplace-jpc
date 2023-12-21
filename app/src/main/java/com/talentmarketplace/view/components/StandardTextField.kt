package com.talentmarketplace.view.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandardTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelResourceID: Int,
    showError: Boolean,
    errorMessage: String?,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary,
            placeholderColor = MaterialTheme.colorScheme.onPrimary
        ),
        value = value,
        onValueChange = { onValueChange(it) },
        isError = showError,
        modifier = modifier.fillMaxWidth(),
        label = { Text(stringResource(id = labelResourceID)) },
        trailingIcon = {
            if (showError)
                Icon(
                    Icons.Filled.Warning, "error",
                    tint = MaterialTheme.colorScheme.error
                )
            else
                Icon(
                    Icons.Default.Edit, contentDescription = "add/edit",
                    tint = Color.Black
                )
        } ,
        supportingText = { ShowSupportText(showError, errorMessage) }
    )
}