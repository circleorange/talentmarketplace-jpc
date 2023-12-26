package com.talentmarketplace.repository

import com.talentmarketplace.model.JobPostModel

interface JobPostRepository {
    fun createJobPost(jobPost: JobPostModel)
    fun getJobPostByID(jobPostID: String): JobPostModel?
    fun getJobPosts(): List<JobPostModel>
    fun deleteJobPost(jobPostID: String)
    fun deleteJobPosts()
    fun updateJobPost(jobPostID: String, jobPostData: JobPostModel)
    fun getJobPostsByUserID(userID: String): List<JobPostModel>
}
