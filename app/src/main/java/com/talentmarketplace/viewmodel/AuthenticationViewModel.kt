package com.talentmarketplace.viewmodel

import androidx.lifecycle.ViewModel
import com.talentmarketplace.model.UserModel
import com.talentmarketplace.repository.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

  //
    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)


    // Authentication states
    sealed class AuthState {
        object Unauthenticated: AuthState()
        object Loading: AuthState()
        data class Authenticated(val user: UserModel): AuthState()
        object InvalidAuthentication: AuthState()
    }
}