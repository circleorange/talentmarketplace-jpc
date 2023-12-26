package com.talentmarketplace.repository.auth

import com.talentmarketplace.model.UserModel

interface BasicAuthRepository {
    suspend fun signIn(email: String, password: String): Result<UserModel>
    suspend fun signUp(fName: String, lName: String, email: String, password: String
    ): Result<UserModel>
    suspend fun signOut()
    suspend fun getCurrentUser(): UserModel?
}