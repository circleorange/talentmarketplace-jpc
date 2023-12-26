package com.talentmarketplace.repository

import com.talentmarketplace.model.JobPostModel

interface JobPostRepository {
    fun addJobPost(jobPost: JobPostModel)
    fun getJobPostByID(jobPostID: String): JobPostModel?
    fun getJobPostings(): List<JobPostModel>
    fun deleteJobPost(jobPostID: String)
    fun deleteJobPost()
    fun updateJobPost(jobPostID: String, jobPostData: JobPostModel)
    fun getJobPostsByUserID(userID: String): List<JobPostModel>
}
