package com.talentmarketplace.repository

import com.talentmarketplace.model.JobPostingModel
import java.util.UUID

interface JobPostingRepository {
    fun addJobPosting(jobPosting: JobPostingModel)
    fun getJobPostingByID(jobPostingID: String): JobPostingModel?
    fun getJobPostings(): List<JobPostingModel>
    fun deleteJobPosting(jobPostingID: String)
    fun deleteJobPostings()
    fun updateJobPosting(jobPostID: String, jobPostData: JobPostingModel)
    fun getJobPostsByUserID(userID: String): List<JobPostingModel>
}
