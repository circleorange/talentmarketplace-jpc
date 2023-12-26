package com.talentmarketplace.repository.auth.firebase

import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.talentmarketplace.repository.auth.GoogleAuthRepository
import kotlinx.coroutines.tasks.await
import timber.log.Timber.i
import java.lang.Exception
import java.util.concurrent.CancellationException
import javax.inject.Inject
import javax.inject.Named

class GoogleAuthRepositoryImpl @Inject constructor(
    @Named("webClientId") private val webClientId: String,
    private val oneTapClient: SignInClient
): GoogleAuthRepository {

    private val auth = Firebase.auth

    override suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception) {
            i("webClientId: $webClientId")
            i("${e.printStackTrace()}")
            // needed as otherwise can mess up coroutine cancellation
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    override suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {

            val user = auth.signInWithCredential(googleCredentials)
                .await()
                .user

            SignInResult(
                data = user?.run {
                    UserData(
                        userID = uid,
                        username = displayName,
                        profilePictureUrl = photoUrl?.toString()
                    )
                },
                errorMessage = null
            )
        }
        catch (e: Exception) {
            i("${e.printStackTrace()}")
            if (e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    override fun getSignedInUser(): UserData? = auth.currentUser?.run {
        UserData(
            userID = uid,
            username = displayName,
            profilePictureUrl = photoUrl?.toString()
        )
    }

    override suspend fun signOut() {
        try {
            oneTapClient
                .signOut()
                .await()
            auth
                .signOut()
        }
        catch (e: Exception) {
            i("${e.printStackTrace()}")
            if (e is CancellationException) throw e
        }
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // true will check if signed in with specific account already
                    // and show only that option, false will show all available accounts
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(webClientId)
                    .build()
            )
            // if only one google account, select such account automatically
            .setAutoSelectEnabled(false)
            .build()
    }
}