package com.talentmarketplace.repository.mem

import com.talentmarketplace.model.JobPostModel
import com.talentmarketplace.repository.JobPostRepository
import javax.inject.Inject


class JobPostMemRepository @Inject constructor() : JobPostRepository {
    private val jobPostings = mutableListOf<JobPostModel>()

    override suspend fun createJobPost(jobPost: JobPostModel) {
        jobPostings.add(jobPost)
    }

    override suspend fun getJobPostByID(jobPostID: String): JobPostModel? {
        return jobPostings.find { it.jobPostID == jobPostID }
    }

    override suspend fun getJobPosts(): List<JobPostModel> = jobPostings

    override suspend fun deleteJobPost(jobPostID: String) {
        jobPostings.removeIf { it.jobPostID == jobPostID }
    }

    override suspend fun deleteJobPosts() {
        jobPostings.clear()
    }

    override suspend fun updateJobPost(jobPostData: JobPostModel) {
        val index = jobPostings.indexOfFirst { it.jobPostID == jobPostData.jobPostID }
        if (index == -1) { return }
        jobPostings[index] = jobPostData
    }

    override suspend fun getJobPostsByUserID(userID: String): List<JobPostModel> {
        return jobPostings.filter { it.userID == userID }
    }
}
