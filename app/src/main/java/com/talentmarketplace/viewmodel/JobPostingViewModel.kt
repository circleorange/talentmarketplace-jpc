package com.talentmarketplace.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talentmarketplace.model.JobPostModel
import com.talentmarketplace.repository.JobPostRepository
import com.talentmarketplace.repository.auth.BasicAuthRepository
import com.talentmarketplace.view.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber.i
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class JobPostingViewModel @Inject constructor(
    private val repository: JobPostRepository,
    private val basicAuthRepository: BasicAuthRepository
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
        viewModelScope.launch { _navEvent.emit(Routes.Job.List.route) }
        i("JobPostListViewModel.onJobPost.redirect")
    }

    // Expose job post details for composable
    private val _jobPostDetails = MutableStateFlow<JobPostModel?>(null)
    val jobPostDetails = _jobPostDetails.asStateFlow()

    fun getJobPostByID(id: String) {
        viewModelScope.launch {
            repository.getJobPostByID(id)?.let { jobPost ->
                companyName.value = jobPost.companyName
                title.value = jobPost.title
                description.value = jobPost.description
                payRange.value = jobPost.payRange
                startDate.value = jobPost.startDate
            }
        }
    }

    fun deleteJobPost(jobPostID: String) {
        i("JobPostViewModel.deleteJobPost.id: $jobPostID")
        repository.deleteJobPost(jobPostID)
    }

    fun updateJobPost(jobPostID: String) {
        viewModelScope.launch {
            i("JobPostViewModel.updateJobPost.id: $jobPostID")
            val signedInUser = basicAuthRepository.getCurrentUser()
            val jobPost = JobPostModel(
                userID = signedInUser!!.uid,
                companyName = companyName.value,
                title = title.value,
                description = description.value,
                payRange = payRange.value,
                startDate = startDate.value
            )
            repository.updateJobPost(jobPostID, jobPost)
        }
    }

    fun addJobPosting() {
        viewModelScope.launch {
            // Only valid inputs past this point
            val signedInUser = basicAuthRepository.getCurrentUser()
            val jobPost = JobPostModel(
                userID = signedInUser!!.uid,
                companyName = companyName.value,
                title = title.value,
                description = description.value,
                payRange = payRange.value,
                startDate = startDate.value
            )
            repository.createJobPost(jobPost)
            i("JobPostingViewModel.addJobPost: $jobPost")
        }
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
