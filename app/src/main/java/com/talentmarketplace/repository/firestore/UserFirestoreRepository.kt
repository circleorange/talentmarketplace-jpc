package com.talentmarketplace.repository.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.talentmarketplace.model.FirestoreUserModel
import kotlinx.coroutines.tasks.await

class UserFirestoreRepository {
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    suspend fun addUserToFirestore(user: FirestoreUserModel) {
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
}