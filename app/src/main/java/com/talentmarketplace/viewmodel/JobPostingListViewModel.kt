package com.talentmarketplace.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talentmarketplace.model.JobPostingModel
import com.talentmarketplace.repository.JobPostingRepository
import com.talentmarketplace.repository.auth.BasicAuthRepository
import com.talentmarketplace.view.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber.i
import javax.inject.Inject

@HiltViewModel
class JobPostingListViewModel @Inject constructor(
    private val repository: JobPostingRepository,
    private val basicAuthRepository: BasicAuthRepository
) : ViewModel() {

    // Expose job posts
    private val _jobPostings = MutableStateFlow<List<JobPostingModel>>(emptyList())
    val jobPostings = _jobPostings.asStateFlow()

    private fun getJobPosts() {
        // coroutine setup to handle async operations
        viewModelScope.launch {
            val signedInUser = basicAuthRepository.getCurrentUser()
            i("JobPostListViewModel.getJobPosts.userId: $signedInUser.id")
            _jobPostings.value = repository.getJobPostsByUserID(signedInUser!!.id)
        }
        i("JobPostingListViewModel.getJobPostings.value: ${_jobPostings.value}")
    }

    // Expose SharedFlow to emit navigation route
    private val _navCmd = MutableSharedFlow<String>()
    val navCmd = _navCmd.asSharedFlow()

    fun onClickJobPost(id: String) {
        viewModelScope.launch {
            _navCmd.emit(Routes.Job.Get.byID(id))
        }
        i("JobPostingListViewModel.onClickJobPost.id: $id")
    }

    init {
        i("JobPostingListViewModel.init")
        getJobPosts()
    }
}