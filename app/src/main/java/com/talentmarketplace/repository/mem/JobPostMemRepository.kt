package com.talentmarketplace.repository.mem

import com.talentmarketplace.model.JobPostModel
import com.talentmarketplace.repository.JobPostRepository
import javax.inject.Inject


class JobPostMemRepository @Inject constructor() : JobPostRepository {
    private val jobPostings = mutableListOf<JobPostModel>()

    override fun createJobPost(jobPost: JobPostModel) {
        jobPostings.add(jobPost)
    }

    override fun getJobPostByID(jobPostID: String): JobPostModel? {
        return jobPostings.find { it.id == jobPostID }
    }

    override fun getJobPosts(): List<JobPostModel> = jobPostings

    override fun deleteJobPost(jobPostID: String) {
        jobPostings.removeIf { it.id == jobPostID }
    }

    override fun deleteJobPosts() {
        jobPostings.clear()
    }

    override fun updateJobPost(jobPostID: String, jobPostData: JobPostModel) {
        val index = jobPostings.indexOfFirst { it.id == jobPostID }
        if (index == -1) { return }
        jobPostings[index] = jobPostData
    }

    override fun getJobPostsByUserID(userID: String): List<JobPostModel> {
        return jobPostings.filter { it.userID == userID }
    }
}
