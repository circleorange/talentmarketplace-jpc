package com.talentmarketplace.model

import java.time.LocalDate
import java.util.UUID

data class JobPostingModel(
    val id: UUID = UUID.randomUUID(),
    val companyName : String,
    val title : String,
    val description : String,
    val payRange : ClosedFloatingPointRange<Float>,
    val startDate : LocalDate
)
