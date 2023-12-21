package com.talentmarketplace.view.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ShowSupportText(isError : Boolean, errorMessage: String?)
{
    if (isError)
        Text(
            text = errorMessage!!,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.error,
        )
    else Text(text = "")
}