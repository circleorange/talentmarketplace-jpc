package com.talentmarketplace.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talentmarketplace.model.JobPostingModel
import com.talentmarketplace.repository.JobPostingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber.i
import java.time.LocalDate
import java.util.UUID
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

    // Expose navigation event to redirect user after successful event
    private val _navEvent = MutableSharedFlow<String>()
    val navEvent = _navEvent.asSharedFlow()

    fun onJobPostRedirect() {
        viewModelScope.launch { _navEvent.emit("Home") }
        i("JobPostListViewModel.onJobPost.redirect")
    }

    // Expose job post details for composable
    private val _jobPostDetails = MutableStateFlow<JobPostingModel?>(null)
    val jobPostDetails = _jobPostDetails.asStateFlow()

    fun getJobPostByID(id: UUID) {
        viewModelScope.launch {
            repository.getJobPostingByID(id)?.let { jobPost ->
                companyName.value = jobPost.companyName
                title.value = jobPost.title
                description.value = jobPost.description
                payRange.value = jobPost.payRange
                startDate.value = jobPost.startDate
            }
        }
    }

    fun deleteJobPost(jobPostID: UUID) {
        i("JobPostViewModel.deleteJobPost.id: $jobPostID")
        repository.deleteJobPosting(jobPostID)
    }

    fun updateJobPost(jobPostID: UUID) {
        i("JobPostViewModel.updateJobPost.id: $jobPostID")
        val jobPost = JobPostingModel(
            companyName = companyName.value,
            title = title.value,
            description = description.value,
            payRange = payRange.value,
            startDate = startDate.value
        )
        repository.updateJobPosting(jobPostID, jobPost)
    }

    fun addJobPosting() {
        // Only valid inputs past this point
        val jobPost = JobPostingModel(
            companyName = companyName.value,
            title = title.value,
            description = description.value,
            payRange = payRange.value,
            startDate = startDate.value
        )
        repository.addJobPosting(jobPost)
        i("JobPostingViewModel.addJobPost: $jobPost")
    }

    fun clearInputFields() {
        companyName.value = ""
        title.value = ""
        description.value = ""
        payRange.value = 50f..1000f
        startDate.value = LocalDate.now()
    }

    // Validate input
    fun isValid(): Boolean {
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
}
