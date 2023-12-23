package com.talentmarketplace.model

import java.util.UUID

data class UserModel(
    val id: UUID = UUID.randomUUID(),
    val email: String,
    val password: String
)