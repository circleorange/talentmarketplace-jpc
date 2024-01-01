package com.talentmarketplace.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talentmarketplace.model.JobPostModel
import com.talentmarketplace.repository.JobPostRepository
import com.talentmarketplace.repository.auth.BasicAuthRepository
import com.talentmarketplace.repository.firestore.UserFirestoreRepository
import com.talentmarketplace.utils.SignInMethodManager
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
class JobPostListViewModel @Inject constructor(
    private val jobRepository: JobPostRepository,
    private val signInMethodManager: SignInMethodManager,
    private val userRepository: UserFirestoreRepository,
) : ViewModel() {

    private val signInMethod: String
        get() = signInMethodManager.getSignInMethod()

    // Expose job posts
    private val _jobPosts = MutableStateFlow<List<JobPostModel>>(emptyList())
    val jobPostings = _jobPosts.asStateFlow()

    private val _filter = mutableStateOf("own")
    val filter: State<String> = _filter

    fun showAllPosts() {
        _filter.value = "all"
        getJobPosts()
    }

    fun showOwnPosts() {
        _filter.value = "own"
        getJobPostsByUser()
    }

    fun getJobPosts() {
        viewModelScope.launch {
            i("getJobPosts")
            _jobPosts.value = jobRepository.getJobPosts()
        }
    }

    fun getJobPostsByUser() {
        viewModelScope.launch {
            i("getJobPostsByUser")
            val currentUser = userRepository.getCurrentUser()!!
            _jobPosts.value = jobRepository.getJobPostsByUserID(currentUser.uid)
        }
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
        showOwnPosts()
    }
}