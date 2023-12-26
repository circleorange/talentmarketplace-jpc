package com.talentmarketplace.repository.auth.basic

import com.talentmarketplace.model.UserModel
import com.talentmarketplace.repository.auth.BasicAuthRepository
import java.lang.Exception
import javax.inject.Inject

class BasicAuthRepositoryImpl @Inject constructor() : BasicAuthRepository {

    private val users = mutableListOf<UserModel>()
    private var signedInUser: UserModel? = null

    override suspend fun signIn(email: String, password: String): Result<UserModel> {
        // find user
        val user = users.firstOrNull { it.email == email && it.password == password }
        return if (user != null) {
            signedInUser = user
            Result.success(user)
        }
        else {
            Result.failure(Exception("Authentication failed"))
        }
    }

    override suspend fun signOut() {
        signedInUser = null
    }

    override suspend fun signUp(
        fName: String,
        lName: String,
        email: String,
        password: String
    ): Result<UserModel> {
        // check if email taken
        if (users.any { it.email == email }) {
            return Result.failure(Exception("User already exists"))
        }
        // create user
        val newUser = UserModel(
            firstName = fName,
            lastName = lName,
            email = email,
            password = password,
            username = null,
            profilePictureUrl = null,
        )
        users.add(newUser)
        signedInUser = newUser
        return Result.success(newUser)
    }

    override suspend fun getCurrentUser(): UserModel? {
        return signedInUser
    }
}