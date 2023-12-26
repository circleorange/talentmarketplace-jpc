package com.talentmarketplace.model

import java.util.UUID

data class UserModel(
    val uid: String = UUID.randomUUID().toString(),
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String,
    val password: String? = null,
    val username: String? = null,
    val profilePictureUrl: String? = null,
)
