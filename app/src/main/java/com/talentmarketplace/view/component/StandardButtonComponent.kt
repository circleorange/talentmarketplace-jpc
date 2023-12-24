package com.talentmarketplace.view.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun StandardButtonComponent(
    onClick: () -> Unit,
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(20.dp),
    leadingIcon: ImageVector? = null,
    resourceStringID: Int,
    iconValueSpacer: Int = 4,

    ) {
    Button(
        // Button Configurations
        onClick = onClick,
        elevation = elevation,
    ) {
        // Button Contents
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.width(iconValueSpacer.dp))
        Text(text = stringResource(id = resourceStringID))
    }
}