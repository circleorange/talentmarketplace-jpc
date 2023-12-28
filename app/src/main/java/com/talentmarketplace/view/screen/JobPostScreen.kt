package com.talentmarketplace.view.screen

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.talentmarketplace.R
import com.talentmarketplace.view.component.StandardTextField
import com.talentmarketplace.viewmodel.JobPostViewModel
import java.time.LocalDate
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.talentmarketplace.view.component.UserCardComponent
import com.talentmarketplace.view.navigation.LocalNavController
import java.time.format.DateTimeFormatter

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun JobPostScreen(
    viewModel: JobPostViewModel = hiltViewModel(),
    jobPostID: String? = null,
    isEditMode: Boolean = false )
{

    val navController = LocalNavController.current

    // Binding to ViewModel state
    val companyName by viewModel.companyName
    val title by viewModel.title
    val description by viewModel.description
    val payRange by viewModel.payRange
    var selectedDate by viewModel.startDate

    val context = LocalContext.current
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val companyNameError by viewModel.companyNameError
    val titleError by viewModel.titleError
    val descriptionError by viewModel.descriptionError

    if (isEditMode) {
        // Get job post on list view click
        LaunchedEffect(jobPostID) {
            jobPostID?.let {
                viewModel.getJobPostByID(it)
            }
        }
    }

    // Collect exposed navigation commands from view model
    LaunchedEffect(viewModel) {
        viewModel.navEvent.collect {
                route -> navController.navigate(route)
        }
    }

    val showConfirmation = viewModel.showConfirmation.observeAsState(initial = false)
    val showErrorNotPostOwner = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 5.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (isEditMode) {
            UserCardComponent(
                profilePictureURL = viewModel.postOwner.value?.profilePictureUrl,
                email = viewModel.postOwner.value?.email ?: "",
                firstName = "Posted",
                lastName = "By:",
            )
        }

        StandardTextField(
            value = companyName,
            onValueChange = { viewModel.companyName.value = it },
            labelResourceID = R.string.text_companyNameHint,
            showError = companyNameError != null,
            errorMessage = companyNameError
        )

        StandardTextField(
            value = title,
            onValueChange = { viewModel.title.value = it },
            labelResourceID = R.string.text_titleHint,
            showError = titleError != null,
            errorMessage = titleError
        )

        StandardTextField(
            value = description,
            onValueChange = { viewModel.description.value = it },
            labelResourceID = R.string.text_jobDescriptionHint,
            showError = descriptionError != null,
            errorMessage = descriptionError
        )

        Text(text = "Selected range: ${payRange.start.toInt()} - ${payRange.endInclusive.toInt()}")

        RangeSlider(
            value = payRange,
            onValueChange = { viewModel.payRange.value = it },
            valueRange = 0f..5000f,
            steps = 500,
            onValueChangeFinished = {  } )

        Button(onClick = {
            val datePickerDialog = DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                },
                selectedDate.year,
                selectedDate.monthValue - 1,
                selectedDate.dayOfMonth
            )
            datePickerDialog.show() }) {
            Text(text = "Start Date: ", style = MaterialTheme.typography.labelLarge)
            Text(text = selectedDate.format(formatter))
        }

        Row {
            Button(
                onClick = {
                    // Exit early if invalid input, no need for nested if statements
                    if (!viewModel.isValid()) {
                        return@Button
                    }
                    if (viewModel.currentUserID.value != viewModel.postOwnerID.value) {
                        showErrorNotPostOwner.value = true
                        return@Button
                    }
                    if (isEditMode) {
                        viewModel.updateJobPost(jobPostID!!)
                    }
                    else {
                        viewModel.addJobPost()
                    }
                    viewModel.onJobPostRedirect()
                          },
                elevation = ButtonDefaults.buttonElevation(20.dp) ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
                Spacer(modifier = Modifier.width(width = 4.dp))
                Text(
                    text = if (isEditMode) {
                        stringResource(R.string.btn_save)
                    } else {
                        stringResource(R.string.button_addJobPosting)
                    }
                )
            }
            
            Spacer(
                modifier = Modifier.width(16.dp),
            )

            if (isEditMode) {
                Button(
                    onClick = {
                        if (viewModel.currentUserID.value != viewModel.postOwnerID.value) {
                            showErrorNotPostOwner.value = true
                            return@Button
                        }
                        viewModel.deleteJobPost(jobPostID!!)
                        viewModel.onJobPostRedirect()
                    },
                    elevation = ButtonDefaults.buttonElevation(20.dp)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Add")
                    Spacer(modifier = Modifier.width(width = 4.dp))
                    Text(stringResource(id = R.string.button_deleteJobPost))
                }
            }
        }
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
        if (showErrorNotPostOwner.value) {
            Snackbar(
                modifier = Modifier.padding(16.dp),
                action = {
                    TextButton(onClick = { showErrorNotPostOwner.value = false }) {
                        Text("OK")
                    }
                }
            ) {
                Text("You are not the owner of this post.")
            }
        }
    }
}
