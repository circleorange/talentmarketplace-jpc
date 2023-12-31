package com.talentmarketplace.repository

import com.talentmarketplace.data.TestJobPostingData
import com.talentmarketplace.model.JobPostModel
import com.talentmarketplace.repository.mem.JobPostMemRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.util.UUID

class JobPostingMemRepositoryTest {

    private lateinit var repository: JobPostMemRepository

    @Before
    fun setup() {
        repository = JobPostMemRepository()
    }

    @After
    fun cleanup() {
        repository.deleteJobPost()
    }

    @Test
    fun `addJobPosting - success`() {
        val jobPosting = TestJobPostingData.jobPostingAtGoogle

        repository.createJobPost(jobPosting)

        val response = repository.getJobPosts()

        // Verify job posting is created
        assertTrue(response.contains(jobPosting))
    }

    @Test
    fun `getJobPostings - success`() {
        val jobPosting1 = TestJobPostingData.jobPostingAtGoogle
        val jobPosting2 = TestJobPostingData.jobPostingAtMicrosoft
        val jobPosting3 = TestJobPostingData.jobPostingAtContruction

        repository.createJobPost(jobPosting1)
        repository.createJobPost(jobPosting2)
        repository.createJobPost(jobPosting3)

        // Verify multiple postings exist
        assertEquals(3, repository.getJobPosts().size)
    }

    @Test
    fun `getJobPostingByID - success`() {
        // Can't use test data here as it's not possible to set ID otherwise
        val id = UUID.randomUUID()
        val jobPosting = JobPostModel(
            uid = id,
            companyName = "Google",
            title = "Software Engineer",
            description = "Development of web app",
            payRange = 15.0f..999.0f,
            startDate = LocalDate.of(2023, 12, 21)
        )

        repository.createJobPost(jobPosting)

        // Verify same job posting is returned as requested
        assertEquals(jobPosting, repository.getJobPostByID(id))
    }

    @Test
    fun `getJobPostingByID - invalid ID`() {
        // Create posting so memory is not empty
        val jobPosting1 = TestJobPostingData.jobPostingAtGoogle
        val jobPosting2 = TestJobPostingData.jobPostingAtMicrosoft
        val jobPosting3 = TestJobPostingData.jobPostingAtContruction

        repository.createJobPost(jobPosting1)
        repository.createJobPost(jobPosting2)
        repository.createJobPost(jobPosting3)

        // Try to retrieve posting using random ID, should not exist
        val invalidID = UUID.randomUUID()

        // Verify postings are not returned
        assertNull(repository.getJobPostByID(invalidID))
    }

    @Test
    fun `deleteJobPostings - success`() {
        val jobPosting1 = TestJobPostingData.jobPostingAtGoogle
        val jobPosting2 = TestJobPostingData.jobPostingAtMicrosoft
        val jobPosting3 = TestJobPostingData.jobPostingAtContruction

        repository.createJobPost(jobPosting1)
        repository.createJobPost(jobPosting2)
        repository.createJobPost(jobPosting3)

        // Verify multiple postings exist
        assertEquals(3, repository.getJobPosts().size)

        repository.deleteJobPost()

        // Verify all postings deleted
        assertEquals(0, repository.getJobPosts().size)
    }

    @Test
    fun `deleteJobPosting - success`() {
        val id = UUID.randomUUID()
        val jobPosting = JobPostModel(
            uid = id,
            companyName = "Google",
            title = "Software Engineer",
            description = "Development of web app",
            payRange = 15.0f..999.0f,
            startDate = LocalDate.of(2023, 12, 21)
        )

        repository.createJobPost(jobPosting)

        // Verify same job posting is returned as requested
        assertEquals(jobPosting, repository.getJobPostByID(id))

        repository.deleteJobPost(id)

        // Verify job posting no longer exists
        assertNull(repository.getJobPostByID(id))
    }


    @Test
    fun `updateJobPosting - success`() {
        val id = UUID.randomUUID()
        val jobPosting = JobPostModel(
            uid = id,
            companyName = "Google",
            title = "Software Engineer",
            description = "Development of web app",
            payRange = 15.0f..999.0f,
            startDate = LocalDate.of(2023, 12, 21)
        )

        repository.createJobPost(jobPosting)

        // Verify company name is Google
        val response = repository.getJobPostByID(id)
        assertEquals(jobPosting.companyName, response!!.companyName)

        // Update company name
        val newCompanyName = "Alphabet"
        jobPosting.companyName = newCompanyName

        val updatedJobPosting = repository.updateJobPost(jobPosting)

        // Verify company name is Updated
        assertEquals(newCompanyName, updatedJobPosting!!.companyName)
    }
}