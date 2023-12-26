package com.talentmarketplace.repository.auth.firebase

import com.talentmarketplace.model.UserModel

data class SignInResult(
    val data: UserModel?,
    val errorMessage: String?
)

data class UserData(
    val userID: String,
    val username: String?,
    val profilePictureUrl: String?
)

