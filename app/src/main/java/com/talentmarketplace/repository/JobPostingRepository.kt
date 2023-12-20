package com.talentmarketplace.repository

import com.talentmarketplace.model.JobPostingModel

interface JobPostingRepository {
    fun addJobPosting(jobPosting: JobPostingModel)

    fun getJobPostings(): List<JobPostingModel>
}