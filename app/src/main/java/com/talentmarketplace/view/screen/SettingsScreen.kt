package com.talentmarketplace.view.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.talentmarketplace.R
import com.talentmarketplace.view.component.WideButtonComponent
import com.talentmarketplace.view.navigation.LocalNavController
import com.talentmarketplace.viewmodel.AuthenticationViewModel

@Composable
fun SettingsScreen(
    viewModel: AuthenticationViewModel = hiltViewModel()
) {
    val navController = LocalNavController.current
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.signOutEvent.collect {
            route -> navController.navigate(route)
        }
    }

    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            WideButtonComponent(
                onClick = { viewModel.onSignOutButtonPress() },
                label = R.string.btn_singOut
            )
        }
    }
}
