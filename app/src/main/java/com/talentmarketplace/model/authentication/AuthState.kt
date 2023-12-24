package com.talentmarketplace.model.authentication

import com.talentmarketplace.model.UserModel

// Authentication states
sealed class AuthState {
    object Unauthenticated: AuthState()
    object Loading: AuthState()
    data class Authenticated(val user: UserModel): AuthState()
    object InvalidAuthentication: AuthState()
}