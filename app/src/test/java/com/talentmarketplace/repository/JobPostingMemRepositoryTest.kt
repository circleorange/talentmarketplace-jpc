package com.talentmarketplace.repository

import com.talentmarketplace.data.TestJobPostingData
import com.talentmarketplace.model.JobPostingModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test
import timber.log.Timber.i
import java.time.LocalDate
import java.util.UUID

class JobPostingMemRepositoryTest {

    private lateinit var repository: JobPostingMemRepository

    @Before
    fun setup() {
        repository = JobPostingMemRepository()
    }

    @After
    fun cleanup() {
        repository.deleteJobPostings()
    }

    @Test
    fun `addJobPosting - success`() {
        val jobPosting = TestJobPostingData.jobPostingAtGoogle

        repository.addJobPosting(jobPosting)

        val response = repository.getJobPostings()

        // Verify job posting is created
        assertTrue(response.contains(jobPosting))
    }

    @Test
    fun `getJobPostings - success`() {
        val jobPosting1 = TestJobPostingData.jobPostingAtGoogle
        val jobPosting2 = TestJobPostingData.jobPostingAtMicrosoft
        val jobPosting3 = TestJobPostingData.jobPostingAtContruction

        repository.addJobPosting(jobPosting1)
        repository.addJobPosting(jobPosting2)
        repository.addJobPosting(jobPosting3)

        // Verify multiple postings exist
        assertEquals(3, repository.getJobPostings().size)
    }

    @Test
    fun `getJobPostingByID - success`() {
        // Can't use test data here as it's not possible to set ID otherwise
        val id = UUID.randomUUID()
        val jobPosting = JobPostingModel(
            id = id,
            companyName = "Google",
            title = "Software Engineer",
            description = "Development of web app",
            payRange = 15.0f..999.0f,
            startDate = LocalDate.of(2023, 12, 21)
        )

        repository.addJobPosting(jobPosting)

        // Verify same job posting is returned as requested
        assertEquals(jobPosting, repository.getJobPostingByID(id))
    }

    @Test
    fun `getJobPostingByID - invalid ID`() {
        // Create posting so memory is not empty
        val jobPosting1 = TestJobPostingData.jobPostingAtGoogle
        val jobPosting2 = TestJobPostingData.jobPostingAtMicrosoft
        val jobPosting3 = TestJobPostingData.jobPostingAtContruction

        repository.addJobPosting(jobPosting1)
        repository.addJobPosting(jobPosting2)
        repository.addJobPosting(jobPosting3)

        // Try to retrieve posting using random ID, should not exist
        val invalidID = UUID.randomUUID()

        // Verify postings are not returned
        assertNull(repository.getJobPostingByID(invalidID))
    }

    @Test
    fun `deleteJobPostings - success`() {
        val jobPosting1 = TestJobPostingData.jobPostingAtGoogle
        val jobPosting2 = TestJobPostingData.jobPostingAtMicrosoft
        val jobPosting3 = TestJobPostingData.jobPostingAtContruction

        repository.addJobPosting(jobPosting1)
        repository.addJobPosting(jobPosting2)
        repository.addJobPosting(jobPosting3)

        // Verify multiple postings exist
        assertEquals(3, repository.getJobPostings().size)

        repository.deleteJobPostings()

        // Verify all postings deleted
        assertEquals(0, repository.getJobPostings().size)
    }

    @Test
    fun `deleteJobPosting - success`() {
        val id = UUID.randomUUID()
        val jobPosting = JobPostingModel(
            id = id,
            companyName = "Google",
            title = "Software Engineer",
            description = "Development of web app",
            payRange = 15.0f..999.0f,
            startDate = LocalDate.of(2023, 12, 21)
        )

        repository.addJobPosting(jobPosting)

        // Verify same job posting is returned as requested
        assertEquals(jobPosting, repository.getJobPostingByID(id))

        repository.deleteJobPosting(id)

        // Verify job posting no longer exists
        assertNull(repository.getJobPostingByID(id))
    }


    @Test
    fun `updateJobPosting - success`() {
        val id = UUID.randomUUID()
        val jobPosting = JobPostingModel(
            id = id,
            companyName = "Google",
            title = "Software Engineer",
            description = "Development of web app",
            payRange = 15.0f..999.0f,
            startDate = LocalDate.of(2023, 12, 21)
        )

        repository.addJobPosting(jobPosting)

        // Verify company name is Google
        val response = repository.getJobPostingByID(id)
        assertEquals(jobPosting.companyName, response!!.companyName)

        // Update company name
        val newCompanyName = "Alphabet"
        jobPosting.companyName = newCompanyName

        val updatedJobPosting = repository.updateJobPosting(jobPosting)

        // Verify company name is Updated
        assertEquals(newCompanyName, updatedJobPosting!!.companyName)
    }
}