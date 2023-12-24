package com.talentmarketplace.model

import java.time.LocalDate
import java.util.UUID

data class JobPostingModel(
    val id: UUID = UUID.randomUUID(),
    val userID: UUID,
    var companyName : String,
    var title : String,
    var description : String,
    var payRange : ClosedFloatingPointRange<Float>,
    var startDate : LocalDate
)
