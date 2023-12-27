package com.talentmarketplace.repository

import com.talentmarketplace.model.FirestoreUserModel
import com.talentmarketplace.model.UserModel

interface UserRepository {
    suspend fun createUser(user: UserModel)
    suspend fun getCurrentUser(): UserModel?
    suspend fun signUp(email: String, password: String): Result<UserModel>
    suspend fun signIn(email: String, password: String): Result<UserModel>
    suspend fun signOut()
}