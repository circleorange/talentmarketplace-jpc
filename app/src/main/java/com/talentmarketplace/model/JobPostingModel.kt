package com.talentmarketplace.model

import java.time.LocalDate

data class JobPostingModel(
    val companyName : String,
    val title : String,
    val description : String,
    val payRange : ClosedFloatingPointRange<Float>,
    val startDate : LocalDate
)
