package com.talentmarketplace.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.talentmarketplace.model.JobPostingModel
import com.talentmarketplace.repository.JobPostingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber.i
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class JobPostingViewModel @Inject constructor(
    private val repository: JobPostingRepository
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

    // Persistence Section
    private val _jobPostings = MutableLiveData<List<JobPostingModel>>()
    val jobPostings: LiveData<List<JobPostingModel>> = _jobPostings

    fun addJobPosting() {
        if (!isValid()) { return }
        // Only valid inputs past this point
        val jobPosting = JobPostingModel(
            companyName = companyName.value,
            title = title.value,
            description = description.value,
            payRange = payRange.value,
            startDate = startDate.value )
        repository.addJobPosting(jobPosting)
        _jobPostings.value = repository.getJobPostings()

        i("_jobPostings.value: ${_jobPostings.value}")
        // TODO: Go to job post listing
    }
}
