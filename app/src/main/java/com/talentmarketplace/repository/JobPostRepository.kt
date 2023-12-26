package com.talentmarketplace.repository

import com.talentmarketplace.model.JobPostModel

interface JobPostRepository {
    suspend fun createJobPost(jobPost: JobPostModel)
    suspend fun getJobPostByID(jobPostID: String): JobPostModel?
    suspend fun getJobPosts(): List<JobPostModel>
    suspend fun deleteJobPost(jobPostID: String)
    suspend fun deleteJobPosts()
    suspend fun updateJobPost(jobPostID: String, jobPostData: JobPostModel)
    suspend fun getJobPostsByUserID(userID: String): List<JobPostModel>
}
