package com.talentmarketplace.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talentmarketplace.model.UserModel
import com.talentmarketplace.repository.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber.i
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authRepository.signIn(email, password)
            _authState.value = if (result.isSuccess) {
                AuthState.Authenticated(result.getOrNull()!!)
            }
            else { AuthState.InvalidAuthentication }
            i("AuthenticationViewModel.signIn.value: ${_authState.value}")
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authRepository.signUp(email, password)
            _authState.value = if (result.isSuccess) {
                AuthState.Authenticated(result.getOrNull()!!)
            }
            else { AuthState.InvalidAuthentication }
            i("AuthenticationViewModel.signUp.value: ${_authState.value}")
        }
    }

    fun signOut() {
        // TODO: sign user out
    }


    // Authentication states
    sealed class AuthState {
        object Unauthenticated: AuthState()
        object Loading: AuthState()
        data class Authenticated(val user: UserModel): AuthState()
        object InvalidAuthentication: AuthState()
    }
}