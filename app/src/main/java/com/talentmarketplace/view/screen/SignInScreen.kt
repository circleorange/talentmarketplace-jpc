package com.talentmarketplace.view.screen

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.talentmarketplace.R
import com.talentmarketplace.repository.auth.firebase.GoogleAuthState
import com.talentmarketplace.view.component.HeaderLabelComponent
import com.talentmarketplace.view.component.HorizontalDividerComponent
import com.talentmarketplace.view.component.StandardTextField
import com.talentmarketplace.view.component.WideButtonComponent
import com.talentmarketplace.view.navigation.LocalNavController
import com.talentmarketplace.viewmodel.AuthenticationViewModel

@Composable
fun SignInScreen(
    viewModel: AuthenticationViewModel = hiltViewModel(),
) {
    val navController = LocalNavController.current
    val context = LocalContext.current

    val email by viewModel.email
    val password by viewModel.password

    // Requires validation
    LaunchedEffect(viewModel) {
        viewModel.signInEvent.collect {
            route -> navController.navigate(route)
        }
    }

    // Routing
    LaunchedEffect(viewModel) {
        viewModel.signUpEvent.collect {
                route -> navController.navigate(route)
        }
    }

    /**
    val state by viewModel.googleAuthState
    LaunchedEffect(state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }
    **/

    // Prepare the launcher for the Google Sign-In intent
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.processGoogleSignInResult(result.data)
        } else {
            // Handle the error or cancellation
        }
    }

    val googleSignInIntentSender by viewModel.googleSignInRequest.collectAsState(initial = null)
    LaunchedEffect(googleSignInIntentSender) {
        googleSignInIntentSender?.let { intentSender ->
            googleSignInLauncher.launch(IntentSenderRequest.Builder(intentSender).build())
            viewModel.resetGoogleSignInRequest()
        }
    }

    Surface (
        color = Color.White,
        modifier = Modifier.fillMaxSize()
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderLabelComponent(value = stringResource(id = R.string.auth_signIn))

            StandardTextField(
                value = email,
                onValueChange = { viewModel.email.value = it },
                labelResourceID = R.string.input_label_email,
                showError = viewModel.emailErrorType.value != null,
                errorMessage = context.getString(R.string.err_email_empty),
                leadingIcon = Icons.Filled.Email
            )

            StandardTextField(
                value = password,
                onValueChange = { viewModel.password.value = it },
                labelResourceID = R.string.input_label_password,
                showError = viewModel.passwordErrorType.value != null,
                errorMessage = context.getString(R.string.err_password_empty),
                leadingIcon = Icons.Filled.Lock
            )

            WideButtonComponent(
                onClick = { viewModel.signIn(email, password) },
                label = R.string.btn_singIn
            )

            Spacer(modifier = Modifier.height(20.dp))

            HorizontalDividerComponent(
                text = R.string.divider_or
            )

            WideButtonComponent(
                onClick = {
                    googleSignInIntentSender?.let { intentSender ->
                        googleSignInLauncher.launch(IntentSenderRequest.Builder(intentSender).build())
                        viewModel.resetGoogleSignInRequest() // Should be called here
                    } ?: viewModel.initiateGoogleSignIn()
                },
                label = R.string.btn_googleSignIn
            )
            
            HorizontalDividerComponent(
                text = R.string.divider_noAccount,
            )
            
            WideButtonComponent(
                onClick = { viewModel.signUp() },
                label = R.string.btn_singUp
            )
        }
    }
}