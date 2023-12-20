package com.talentmarketplace.view.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.talentmarketplace.R
import com.talentmarketplace.main.StandardTextField
import com.talentmarketplace.view.theme.JobPostingJPCTheme
import com.talentmarketplace.viewmodel.JobPostingViewModel
import java.time.LocalDate
import androidx.compose.ui.platform.LocalContext
import java.time.format.DateTimeFormatter
import androidx.lifecycle.viewmodel.compose.viewModel
import com.talentmarketplace.model.JobPostingModel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
fun JobPostingScreen(viewModel: JobPostingViewModel = viewModel()) {

    // Binding to ViewModel's state
    // Main Job Posting Fields
    var companyName by viewModel.companyName
    var title by viewModel.title
    var description by viewModel.description
    val payRange by viewModel.payRange
    var selectedDate by viewModel.startDate
    val context = LocalContext.current
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    // Errors
    val companyNameError by viewModel.companyNameError
    val titleError by viewModel.titleError
    val descriptionError by viewModel.descriptionError

    JobPostingJPCTheme {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                onValueChangeFinished = {  }
            )

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
                datePickerDialog.show()
            }) {
                Text(text = "Start Date: ", style = MaterialTheme.typography.labelLarge)
                Text(text = selectedDate.format(formatter))
            }

            Button(
                onClick = { viewModel.addJobPosting() },
                elevation = ButtonDefaults.buttonElevation(20.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
                Spacer(modifier = Modifier.width(width = 4.dp))
                Text(stringResource(id = R.string.button_addJobPosting))
            }
        }
    }
}