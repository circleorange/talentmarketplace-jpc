package com.talentmarketplace.repository

import com.talentmarketplace.model.JobPostingModel
import javax.inject.Inject


class JobPostingMemRepository @Inject constructor() : JobPostingRepository {
    private val jobPostings = mutableListOf<JobPostingModel>()

    override fun addJobPosting(jobPosting: JobPostingModel) {
        jobPostings.add(jobPosting)
    }

    override fun getJobPostings(): List<JobPostingModel> = jobPostings
}