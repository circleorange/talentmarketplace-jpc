package com.talentmarketplace.view.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.talentmarketplace.R
import com.talentmarketplace.view.component.HeaderLabelComponent
import com.talentmarketplace.view.component.StandardTextField
import com.talentmarketplace.view.component.WideButtonComponent
import com.talentmarketplace.viewmodel.UserViewModel

@Composable
fun ProfileScreen(
    viewModel: UserViewModel = hiltViewModel(),
) {

    val firstName = viewModel.firstName.observeAsState()
    val lastName = viewModel.lastName.observeAsState()
    val displayName = viewModel.displayName.observeAsState()
    val email = viewModel.email.observeAsState()
    val profilePictureURL = viewModel.profilePictureUrl.observeAsState()

    val context = LocalContext.current

    val showConfirmation = viewModel.showConfirmation.observeAsState(initial = false)

    viewModel.getCurrentUserDetails()

    Surface (
        color = Color.White,
        modifier = Modifier.fillMaxSize()
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderLabelComponent (
                value = context.getString(R.string.label_profile)
            )

            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {

                    AsyncImage(
                        model = profilePictureURL.value,
                        contentDescription = null,
                        modifier = Modifier
                            .size(64.dp)
                            .align(Alignment.CenterVertically),
                    )

                    Spacer(
                        modifier = Modifier
                            .width(16.dp)
                    )

                    Column {
                        if (firstName.value != null && lastName.value != null) {
                            Text(text = "${firstName.value} ${lastName.value}")
                        }
                        Text(
                            email.value ?: context.getString(R.string.err_generic),
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier
                    .height(30.dp)
            )
            StandardTextField(
                value = firstName.value ?: "",
                onValueChange = { viewModel.firstName.value = it },
                labelResourceID = R.string.label_firstname,
                showError = false,
                errorMessage = context.getString(R.string.err_fistName_empty),
            )

            StandardTextField(
                value = lastName.value ?: "",
                onValueChange = { viewModel.lastName.value = it },
                labelResourceID = R.string.label_lastname,
                showError = false,
                errorMessage = context.getString(R.string.err_lastName_empty),
            )

            WideButtonComponent(
                onClick = { viewModel.updateUserDetails() },
                label = R.string.btn_save
            )

            if (showConfirmation.value) {
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    action = {
                        TextButton(onClick = { viewModel.showConfirmation.value = false }) {
                            Text("OK")
                        }
                    }
                ) {
                    Text("User details updated")
                }
            }
        }
    }
}
