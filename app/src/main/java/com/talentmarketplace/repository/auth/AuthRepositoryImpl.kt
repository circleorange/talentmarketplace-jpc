package com.talentmarketplace.repository.auth

import com.talentmarketplace.model.UserModel
import java.lang.Exception
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor() : AuthRepository {
    private val users = mutableListOf<UserModel>()

    override suspend fun signIn(email: String, password: String): Result<UserModel> {
        // find user
        val user = users.firstOrNull { it.email == email && it.password == password }
        return if (user != null) { Result.success(user) }
        else { Result.failure(Exception("Authentication failed")) }
    }

    override suspend fun signOut() {
        TODO("Not yet implemented")
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
            password = password
        )
        users.add(newUser)
        return Result.success(newUser)
    }
}