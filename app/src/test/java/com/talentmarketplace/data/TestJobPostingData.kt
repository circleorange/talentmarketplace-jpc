package com.talentmarketplace.data

import com.talentmarketplace.model.JobPostingModel
import java.time.LocalDate

object TestJobPostingData {
    val jobPostingAtGoogle = JobPostingModel(
        companyName = "Google",
        title = "Software Engineer",
        description = "Development of web app",
        payRange = 15.0f..999.0f,
        startDate = LocalDate.of(2023, 12, 21)
    )
    val jobPostingAtMicrosoft = JobPostingModel(
        companyName = "Microsoft",
        title = "Network Engineer",
        description = "Set up lab environments",
        payRange = 800.0f..1500.0f,
        startDate = LocalDate.of(2023, 11, 15)
    )
    val jobPostingAtContruction = JobPostingModel(
        companyName = "Builders Ltd.",
        title = "Construction Worker",
        description = "Need more hands to build a house",
        payRange = 1500.0f..3000.0f,
        startDate = LocalDate.of(2024, 1, 2)
    )
}