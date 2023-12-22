package com.talentmarketplace.repository

import com.talentmarketplace.model.JobPostingModel
import java.util.UUID

interface JobPostingRepository {
    fun addJobPosting(jobPosting: JobPostingModel)
    fun getJobPostingByID(jobPostingID: UUID): JobPostingModel?
    fun getJobPostings(): List<JobPostingModel>
    fun deleteJobPosting(jobPostingID: UUID)
    fun deleteJobPostings()
    fun updateJobPosting(jobPosting: JobPostingModel): JobPostingModel?
}
