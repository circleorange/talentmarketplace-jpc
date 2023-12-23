package com.talentmarketplace.view.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.talentmarketplace.R
import com.talentmarketplace.model.authentication.EmailErrorType
import com.talentmarketplace.view.component.HeaderLabelComponent
import com.talentmarketplace.viewmodel.AuthenticationViewModel

@Composable
fun SignUpScreen(
    viewModel: AuthenticationViewModel = hiltViewModel(),
    navController: NavController
) {

    // User Fields
    val email by viewModel.email
    val password by viewModel.password

    // User Field Messages
    val context = LocalContext.current
    val emailErrorMessage = when (viewModel.emailErrorType.value) {
        EmailErrorType.EMPTY -> context.getString(R.string.err_email_empty)
        EmailErrorType.INVALID -> context.getString(R.string.err_email_invalid)
        EmailErrorType.TAKEN -> context.getString(R.string.err_email_taken)
        else -> null
    }

    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize()
    ) {
        HeaderLabelComponent(value = stringResource(id = R.string.account_registration))

    }
}

@Preview
@Composable
fun SignUpScreenPreview(
) {
    SignUpScreen()
}