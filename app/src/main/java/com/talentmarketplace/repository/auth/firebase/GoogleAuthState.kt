package com.talentmarketplace.repository.auth.firebase

data class GoogleAuthState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)