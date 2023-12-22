package com.talentmarketplace.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talentmarketplace.model.JobPostingModel
import com.talentmarketplace.repository.JobPostingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber.i
import javax.inject.Inject

@HiltViewModel
class JobPostingListViewModel @Inject constructor(
    private val repository: JobPostingRepository
) : ViewModel() {

    private val _jobPostings = MutableStateFlow<List<JobPostingModel>>(emptyList())
    val jobPostings: StateFlow<List<JobPostingModel>> = _jobPostings

    init {
        i("JobPostingListViewModel.init.getJobPostings(): ${repository.getJobPostings()}")
        getJobPostings()
    }

    private fun getJobPostings() {
        // coroutine setup to handle async operations
        viewModelScope.launch { _jobPostings.value = repository.getJobPostings() }
        i("JobPostingListViewModel.jobPostings.value: ${jobPostings.value} ${_jobPostings.value}")
    }
}