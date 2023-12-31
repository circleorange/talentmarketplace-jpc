package com.talentmarketplace.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.talentmarketplace.model.JobPostModel
import com.talentmarketplace.model.UserModel
import com.talentmarketplace.repository.JobPostRepository
import com.talentmarketplace.repository.UserRepository
import com.talentmarketplace.utils.FirestoreConversionManager.Companion.localDateFromTimestamp
import com.talentmarketplace.utils.FirestoreConversionManager.Companion.localDateToTimestamp
import com.talentmarketplace.utils.FirestoreConversionManager.Companion.payRangeFromString
import com.talentmarketplace.utils.FirestoreConversionManager.Companion.payRangeToString
import com.talentmarketplace.view.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber.i
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class JobPostViewModel @Inject constructor(
    private val jobRepository: JobPostRepository,
    private val userRepository: UserRepository,
): ViewModel() {

    // User Variables
    var currentUserID = mutableStateOf("")
    var postOwnerID = mutableStateOf("")
    var postOwner = mutableStateOf<UserModel?>(null)

    // Job Post Variables
    val jobPostID = mutableStateOf<String?>(null)
    var companyName = mutableStateOf("")
    var title = mutableStateOf("")
    var description = mutableStateOf("")
    var payRange = mutableStateOf(50f..1000f)
    var startDate = mutableStateOf(LocalDate.now())
    var jobLocation = mutableStateOf<LatLng?>(null)


    var companyNameError = mutableStateOf<String?>(null)
    var titleError = mutableStateOf<String?>(null)
    var descriptionError = mutableStateOf<String?>(null)

    // Confirmation Message
    val showConfirmation = MutableLiveData<Boolean>(false)

    // Expose navigation event to redirect user after successful event
    private val _navEvent = MutableSharedFlow<String>()
    val navEvent = _navEvent.asSharedFlow()

    private val _jobPost = mutableStateOf<JobPostModel?>(null)

    private suspend fun getCurrentUser() {
            currentUserID.value = userRepository.getCurrentUser()!!.uid
            i("getCurrentUser.end: ${currentUserID.value}")
    }

    private fun getJobPostOwner() {
        viewModelScope.launch {
            i("getJobPostOwner.id: ${postOwnerID.value}")
            postOwner.value = userRepository.getUserByID(postOwnerID.value)
            i("getJobPostOwner.end: ${postOwner.value}")
        }
    }

    fun onJobPostRedirect() {
        viewModelScope.launch { _navEvent.emit(Routes.Job.List.route) }
        i("JobPostListViewModel.onJobPost.redirect")
    }

    fun getJobPostByID(id: String) {
        viewModelScope.launch {
            jobRepository.getJobPostByID(id)?.let {
                _jobPost.value = it
                setJobPostDetails(it)
                i("getJobPostByID: ${_jobPost.value}")
                getCurrentUser()
                getJobPostOwner()
                i("getJobPostByID.end")
            }
        }
    }

    private fun setJobPostDetails(jobPost: JobPostModel) {
        jobPostID.value = jobPost.jobPostID
        postOwnerID.value = jobPost.userID
        companyName.value = jobPost.companyName
        title.value = jobPost.title
        description.value = jobPost.description
        payRange.value = payRangeFromString(jobPost.payRange)
        startDate.value = localDateFromTimestamp(jobPost.startDate)
        jobLocation.value = if (jobPost.latitude != null && jobPost.longitude != null) {
            LatLng(jobPost.latitude, jobPost.longitude)
        } else { null }
    }

    fun deleteJobPost(jobPostID: String) {
        i("JobPostViewModel.deleteJobPost.id: $jobPostID")
        viewModelScope.launch {
            jobRepository.deleteJobPost(jobPostID)
        }
    }

    fun updateJobPost(jobPostID: String) {
        viewModelScope.launch {

            val jobPost = JobPostModel(
                jobPostID = jobPostID,
                userID = currentUserID.value,
                companyName = companyName.value,
                title = title.value,
                description = description.value,
                payRange = payRangeToString(payRange.value),
                startDate = localDateToTimestamp(startDate.value),
                latitude = jobLocation.value?.latitude,
                longitude = jobLocation.value?.longitude,
            )
            i("JobPostViewModel.updateJobPost: $jobPost")
            jobRepository.updateJobPost(jobPost)
            showConfirmation.value = true
        }
    }

    fun addJobPost() {
        viewModelScope.launch {
            getCurrentUser()

            val newJobPost = JobPostModel(
                userID = currentUserID.value,
                companyName = companyName.value,
                title = title.value,
                description = description.value,
                payRange = payRangeToString(payRange.value),
                startDate = localDateToTimestamp(startDate.value),
                latitude = jobLocation.value?.latitude,
                longitude = jobLocation.value?.longitude,
            )
            _jobPost.value = newJobPost
            i("JobPostingViewModel.addJobPost: ${_jobPost.value}")
            jobRepository.createJobPost(newJobPost)
            showConfirmation.value = true
        }
    }

    fun isPostOwner(): Boolean {
        if (currentUserID.value == postOwnerID.value) {
            return true
        }
        return false
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
