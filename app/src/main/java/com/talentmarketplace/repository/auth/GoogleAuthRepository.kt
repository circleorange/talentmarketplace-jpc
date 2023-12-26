package com.talentmarketplace.repository.auth

import android.content.Intent
import android.content.IntentSender
import com.talentmarketplace.repository.auth.firebase.SignInResult
import com.talentmarketplace.repository.auth.firebase.UserData

interface GoogleAuthRepository {

    suspend fun signIn(): IntentSender?
    suspend fun signInWithIntent(intent: Intent): SignInResult
    fun getSignedInUser(): UserData?
    suspend fun signOut()
}