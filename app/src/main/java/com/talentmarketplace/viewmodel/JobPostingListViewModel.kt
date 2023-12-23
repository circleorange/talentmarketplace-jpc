package com.talentmarketplace.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talentmarketplace.model.JobPostingModel
import com.talentmarketplace.repository.JobPostingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber.i
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class JobPostingListViewModel @Inject constructor(
    private val repository: JobPostingRepository
) : ViewModel() {

    // Expose job posts
    private val _jobPostings = MutableStateFlow<List<JobPostingModel>>(emptyList())
    val jobPostings = _jobPostings.asStateFlow()

    private fun getJobPostings() {
        // coroutine setup to handle async operations
        viewModelScope.launch { _jobPostings.value = repository.getJobPostings() }
        i("JobPostingListViewModel.getJobPostings.value: ${_jobPostings.value}")
    }

    // Expose SharedFlow to emit navigation route
    private val _navCmd = MutableSharedFlow<String>()
    val navCmd = _navCmd.asSharedFlow()

    fun onClickJobPost(id: UUID) {
        viewModelScope.launch { _navCmd.emit("Create/${id}") }
        i("JobPostingListViewModel.onClickJobPost.id: $id")
    }

    init {
        i("JobPostingListViewModel.init")
        getJobPostings()
    }
}