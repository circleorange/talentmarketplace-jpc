package com.talentmarketplace.model

import java.util.UUID

data class UserModel(
    val id: String = UUID.randomUUID().toString(),
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val password: String?,
    val username: String?,
    val profilePictureUrl: String?
)
