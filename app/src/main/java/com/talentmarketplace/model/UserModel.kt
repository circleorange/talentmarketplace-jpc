package com.talentmarketplace.model

data class UserModel(
    val uid: String = "",
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String = "",
    val username: String? = null,
    val profilePictureUrl: String? = null,
)
