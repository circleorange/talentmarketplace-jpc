package com.talentmarketplace.view.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.talentmarketplace.R
import com.talentmarketplace.view.component.HeaderLabelComponent
import com.talentmarketplace.view.component.StandardTextField

@Composable
fun SignUpScreen() {

    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize()
    ) {
        HeaderLabelComponent(value = stringResource(id = R.string.account_registration))
    }
}

@Preview
@Composable
fun SignUpScreenPreview() {
    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            HeaderLabelComponent(value = stringResource(id = R.string.account_registration))
            StandardTextField(
                value = ,
                onValueChange = ,
                labelResourceID = ,
                showError = ,
                errorMessage = 
            )
        }
    }
}