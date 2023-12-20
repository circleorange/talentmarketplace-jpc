package com.talentmarketplace.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber.i
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class JobPostingViewModel @Inject constructor(
    // TODO: might need to inject repository in the future
) : ViewModel() {
    var companyName = mutableStateOf("")
    var title = mutableStateOf("")
    var description = mutableStateOf("")
    var payRange = mutableStateOf(50f..1000f)
    var startDate = mutableStateOf(LocalDate.now())

    var companyNameError = mutableStateOf<String?>(null)
    var titleError = mutableStateOf<String?>(null)
    var descriptionError = mutableStateOf<String?>(null)

    private fun isValid(): Boolean {
        var isValid = true

        companyNameError.value = if (companyName.value.isEmpty()) {
            isValid = false
            "Company Name cannot be empty"
        } else null

        titleError.value = if (title.value.isEmpty()) {
            isValid = false
            "Job Title cannot be empty"
        } else null

        descriptionError.value = if (description.value.isEmpty()) {
            isValid = false
            "Job Description cannot be empty"
        } else null

        return isValid
    }

    fun addJobPosting() {
        if (!isValid()) { return }
        // Only valid input past this point
        // var posting = JobPostingModel()
        // TODO: Add job posting to database
        // TODO: Go to job post listing
    }
}