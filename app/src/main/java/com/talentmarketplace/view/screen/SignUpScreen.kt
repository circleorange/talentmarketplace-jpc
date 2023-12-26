package com.talentmarketplace.view.screen

import androidx.activity.result.IntentSenderRequest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.talentmarketplace.R
import com.talentmarketplace.model.authentication.EmailErrorType
import com.talentmarketplace.model.authentication.PasswordErrorType
import com.talentmarketplace.view.component.HeaderLabelComponent
import com.talentmarketplace.view.component.HorizontalDividerComponent
import com.talentmarketplace.view.component.StandardTextField
import com.talentmarketplace.viewmodel.AuthenticationViewModel
import com.talentmarketplace.view.component.WideButtonComponent
import com.talentmarketplace.view.navigation.LocalNavController

@Composable
fun SignUpScreen(
    viewModel: AuthenticationViewModel = hiltViewModel(),
) {

    val navController = LocalNavController.current

    // User Fields
    val firstName by viewModel.firstName
    val lastName by viewModel.lastName
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
    val passwordErrorMessage = when (viewModel.passwordErrorType.value) {
        PasswordErrorType.EMPTY -> context.getString(R.string.err_password_empty)
        else -> null
    }

    LaunchedEffect(viewModel) {
        viewModel.signInEvent.collect {
                route -> navController.navigate(route)
        }
    }

    // Successful Authentication
    LaunchedEffect(viewModel) {
        viewModel.signUpEvent.collect {
            route -> navController.navigate(route)
        }
    }

    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            HeaderLabelComponent(value = stringResource(id = R.string.auth_signUp))

            StandardTextField(
                value = firstName,
                onValueChange = { viewModel.firstName.value = it },
                labelResourceID = R.string.input_label_fname,
                showError = viewModel.fNameFieldErrMsg.value != null,
                errorMessage = context.getString(R.string.err_fistName_empty),
                leadingIcon = Icons.Filled.Email
            )

            StandardTextField(
                value = lastName,
                onValueChange = { viewModel.lastName.value = it },
                labelResourceID = R.string.input_label_lname,
                showError = viewModel.lNameFieldErrMsg.value != null,
                errorMessage = context.getString(R.string.err_lastName_empty),
                leadingIcon = Icons.Filled.Email
            )

            StandardTextField(
                value = email,
                onValueChange = { viewModel.email.value = it },
                labelResourceID = R.string.input_label_email,
                showError = viewModel.emailErrorType.value != null,
                errorMessage = emailErrorMessage,
                leadingIcon = Icons.Filled.Email
            )

            StandardTextField(
                value = password,
                onValueChange = { viewModel.password.value = it },
                labelResourceID = R.string.input_label_password,
                showError = viewModel.passwordErrorType.value != null,
                errorMessage = passwordErrorMessage,
                leadingIcon = Icons.Filled.Lock,
                hideInput = PasswordVisualTransformation()
            )

            WideButtonComponent(
                onClick = {
                    viewModel.signUp(firstName, lastName, email, password)
                },
                label = R.string.btn_singUp
            )

            Spacer(modifier = Modifier.height(20.dp))

            HorizontalDividerComponent(text = R.string.divider_or)

            WideButtonComponent(
                onClick = {
                },
                label = R.string.btn_googleSignUp
            )

            HorizontalDividerComponent(text = R.string.divider_haveAccount)

            WideButtonComponent(
                onClick = { viewModel.signIn() },
                label = R.string.btn_singIn
            )
        }
    }
}
