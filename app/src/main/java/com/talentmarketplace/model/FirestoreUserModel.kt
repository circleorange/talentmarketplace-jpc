package com.talentmarketplace.model

data class FirestoreUserModel(
    val uid: String,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val username: String?,
    val profilePictureUrl: String?
)
