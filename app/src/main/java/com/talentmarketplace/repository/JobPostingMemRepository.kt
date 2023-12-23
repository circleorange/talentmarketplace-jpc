package com.talentmarketplace.repository

import com.talentmarketplace.model.JobPostingModel
import java.util.UUID
import javax.inject.Inject


class JobPostingMemRepository @Inject constructor() : JobPostingRepository {
    private val jobPostings = mutableListOf<JobPostingModel>()

    override fun addJobPosting(jobPosting: JobPostingModel) {
        jobPostings.add(jobPosting)
    }

    override fun getJobPostingByID(jobPostingID: UUID): JobPostingModel? {
        return jobPostings.find { it.id == jobPostingID }
    }

    override fun getJobPostings(): List<JobPostingModel> = jobPostings

    override fun deleteJobPosting(jobPostingID: UUID) {
        jobPostings.removeIf { it.id == jobPostingID }
    }

    override fun deleteJobPostings() {
        jobPostings.clear()
    }

    override fun updateJobPosting(jobPostID: UUID, jobPostData: JobPostingModel) {
        val index = jobPostings.indexOfFirst { it.id == jobPostID }
        if (index == -1) { return }
        jobPostings[index] = jobPostData
    }
}
