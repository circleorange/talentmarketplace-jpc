package com.talentmarketplace.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talentmarketplace.model.authentication.AuthState
import com.talentmarketplace.model.authentication.EmailErrorType
import com.talentmarketplace.model.authentication.PasswordErrorType
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

    // Authentication State
    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    val email = mutableStateOf("")
    val password = mutableStateOf("")

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
            else {
                _emailErrorType.value = EmailErrorType.TAKEN
                AuthState.InvalidAuthentication
            }
            i("AuthenticationViewModel.signUp.value: ${_authState.value}")
        }
    }

    fun signOut() {
        // TODO: sign user out
    }

    // Email Validation
    private val _emailErrorType = mutableStateOf<EmailErrorType?>(null)
    val emailErrorType: State<EmailErrorType?> = _emailErrorType
    fun isValidEmail(email: String): Boolean {
        if (email.isEmpty()) {
            _emailErrorType.value = EmailErrorType.EMPTY
            return false
        }
        if (!email.contains("@")) {
            _emailErrorType.value = EmailErrorType.INVALID
            return false
        }
        return true
    }

    // Password Validation
    private val _passwordErrorType = mutableStateOf<PasswordErrorType?>(null)
    val passwordErrorType: State<PasswordErrorType?> = _passwordErrorType
    fun isValidPassword(password: String): Boolean {
        if (password.isEmpty()) {
            _passwordErrorType.value = PasswordErrorType.EMPTY
            return false
        }
        return true
    }
}
