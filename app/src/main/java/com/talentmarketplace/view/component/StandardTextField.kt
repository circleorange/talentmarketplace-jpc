package com.talentmarketplace.view.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.talentmarketplace.R
import com.talentmarketplace.view.theme.InputBgColor
import com.talentmarketplace.view.theme.componentShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandardTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelResourceID: Int,
    showError: Boolean,
    errorMessage: String?,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    hideInput: VisualTransformation = VisualTransformation.None
) {
    OutlinedTextField(
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary,
            placeholderColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = InputBgColor,
        ),
        modifier = modifier
            .fillMaxWidth()
            .clip(componentShape.large)
            .padding(horizontal = 10.dp),
        leadingIcon = leadingIcon?.let {{
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
            )
        }},
        label = { Text(stringResource(id = labelResourceID)) },
        value = value,
        onValueChange = { onValueChange(it) },
        isError = showError,
        trailingIcon = {
            if (showError)
                Icon(
                    Icons.Filled.Warning,
                    contentDescription = "error",
                    tint = MaterialTheme.colorScheme.error
                )
            else
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "add/edit",
                    tint = Color.Black
                )
        },
        visualTransformation = hideInput,
        supportingText = { ShowSupportText(showError, errorMessage) }
    )
}
