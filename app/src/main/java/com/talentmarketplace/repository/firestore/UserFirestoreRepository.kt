package com.talentmarketplace.repository.firestore

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.toObject
import com.talentmarketplace.model.FirestoreUserModel
import com.talentmarketplace.model.UserModel
import com.talentmarketplace.repository.UserRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserFirestoreRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    db: FirebaseFirestore,
): UserRepository {
    private val usersCollection = db.collection("users")

    override suspend fun createUser(user: UserModel) {
        try {
            usersCollection
                .document(user.uid)
                .set(user)
                .await()
        }
        catch (e: FirebaseFirestoreException) {
            e.printStackTrace()
        }
    }

    override suspend fun getCurrentUser(): UserModel? {
        val currentUser = firebaseAuth.currentUser ?: return null

        return try {
            usersCollection
                .document(currentUser.uid)
                .get()
                .await()
                .toObject<UserModel>()
        }
        catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun signIn(email: String, password: String): Result<UserModel> {
        return try {
            val authResult = firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .await()
            val firebaseUser = authResult.user ?: return Result.failure(
                FirebaseAuthException("No Firebase User", "User not available")
            )
            val user = usersCollection
                .document(firebaseUser.uid)
                .get()
                .await()
                .toObject<UserModel>() ?: throw FirebaseAuthException(
                    "No User Data", "User data not available in Firestore"
                )
            Result.success(user)
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signUp(email: String, password: String): Result<UserModel> {
        return try {
            val authResult = firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .await()

            val firebaseUser = authResult.user ?: return Result.failure(
                FirebaseAuthException("No Firebase User", "User not created successfully")
            )
            val newUser = UserModel(uid = firebaseUser.uid, email = email)
            usersCollection
                .document(firebaseUser.uid)
                .set(newUser)
                .await()

            Result.success(newUser)
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }
}