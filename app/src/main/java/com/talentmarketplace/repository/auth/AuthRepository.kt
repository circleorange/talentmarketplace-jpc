package com.talentmarketplace.repository.auth

import com.talentmarketplace.model.UserModel

interface AuthRepository {
    suspend fun signIn(email: String, password: String): Result<UserModel>
    suspend fun signUp(email: String, password: String): Result<UserModel>
    suspend fun signOut()
}