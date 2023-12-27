package com.talentmarketplace.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talentmarketplace.model.JobPostModel
import com.talentmarketplace.repository.JobPostRepository
import com.talentmarketplace.repository.UserRepository
import com.talentmarketplace.utils.FirestoreConversionManager.Companion.localDateFromTimestamp
import com.talentmarketplace.utils.FirestoreConversionManager.Companion.localDateToTimestamp
import com.talentmarketplace.utils.FirestoreConversionManager.Companion.payRangeFromString
import com.talentmarketplace.utils.FirestoreConversionManager.Companion.payRangeToString
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
class JobPostViewModel @Inject constructor(
    private val jobRepository: JobPostRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    var companyName = mutableStateOf("")
    var title = mutableStateOf("")
    var description = mutableStateOf("")
    var payRange = mutableStateOf(50f..1000f)
    var startDate = mutableStateOf(LocalDate.now())

    var companyNameError = mutableStateOf<String?>(null)
    var titleError = mutableStateOf<String?>(null)
    var descriptionError = mutableStateOf<String?>(null)

    // Confirmation Message
    val showConfirmation = MutableLiveData<Boolean>(false)

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
            jobRepository.getJobPostByID(id)?.let {
                _jobPostDetails.value = it
                setJobPostDetails(it)
            }
        }
        i("JobPostViewModel.getPostByID: $_jobPostDetails")
    }

    fun setJobPostDetails(jobPost: JobPostModel) {
        companyName.value = jobPost.companyName
        title.value = jobPost.title
        description.value = jobPost.description
        payRange.value = payRangeFromString(jobPost.payRange)
        startDate.value = localDateFromTimestamp(jobPost.startDate)
    }

    fun deleteJobPost(jobPostID: String) {
        i("JobPostViewModel.deleteJobPost.id: $jobPostID")
        viewModelScope.launch {
            jobRepository.deleteJobPost(jobPostID)
        }
    }

    fun updateJobPost(jobPostID: String) {
        viewModelScope.launch {
            i("JobPostViewModel.updateJobPost.id: $jobPostID")
            val currentUser = userRepository.getCurrentUser()!!

            val jobPost = JobPostModel(
                jobPostID = jobPostID,
                userID = currentUser.uid,
                companyName = companyName.value,
                title = title.value,
                description = description.value,
                payRange = payRangeToString(payRange.value),
                startDate = localDateToTimestamp(startDate.value),
            )
            jobRepository.updateJobPost(jobPost)
            showConfirmation.value = true
        }
    }

    fun addJobPost() {
        viewModelScope.launch {
            // Only valid inputs past this point
            val currentUser = userRepository.getCurrentUser()!!

            val jobPost = JobPostModel(
                userID = currentUser.uid,
                companyName = companyName.value,
                title = title.value,
                description = description.value,
                payRange = payRangeToString(payRange.value),
                startDate = localDateToTimestamp(startDate.value)
            )
            jobRepository.createJobPost(jobPost)
            showConfirmation.value = true
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
