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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber.i
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    // Emit Navigation Route
    private val _navigationEvent = MutableSharedFlow<String>()
    val navigationEvent = _navigationEvent.asSharedFlow()
    fun onAuthenticationSuccess() {
        viewModelScope.launch { _navigationEvent.emit("Home") }
    }

    // Authentication State
    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    val firstName = mutableStateOf("")
    val lastName = mutableStateOf("")
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

    fun signUp(firstName: String, lastName: String, email: String, password: String) {
        viewModelScope.launch {

            // Validate all Sign Up Fields
            val validEmail = isValidEmail(email)
            val validPassword = isValidPassword(password)
            val validFirstName = isValidFirstName(firstName)
            val validLastName = isValidLastName(lastName)

            // Exit function for invalid input
            if (!validEmail || !validPassword || !validFirstName || !validLastName) {
                i("AuthenticationViewModel.signUp.fail")
                return@launch
            }

            _authState.value = AuthState.Loading
            val result = authRepository.signUp(firstName, lastName, email, password)
            _authState.value = if (result.isSuccess) {
                AuthState.Authenticated(result.getOrNull()!!)
            } else {
                _emailErrorType.value = EmailErrorType.TAKEN
                AuthState.InvalidAuthentication
            }
            i("AuthenticationViewModel.signUp.success: ${_authState.value}")
        }
    }

    fun signOut() {
        // TODO: sign user out
    }

    // Email Validation
    private val _emailErrorType = mutableStateOf<EmailErrorType?>(null)
    val emailErrorType: State<EmailErrorType?> = _emailErrorType
    private fun isValidEmail(email: String): Boolean {
        if (email.isEmpty()) {
            i("AuthenticationViewModel.isValidEmail.false.empty")
            _emailErrorType.value = EmailErrorType.EMPTY
            return false
        }
        if (!email.contains("@")) {
            i("AuthenticationViewModel.isValidEmail.false.invalid")
            _emailErrorType.value = EmailErrorType.INVALID
            return false
        }
        return true
    }

    // Password Validation
    private val _passwordErrorType = mutableStateOf<PasswordErrorType?>(null)
    val passwordErrorType: State<PasswordErrorType?> = _passwordErrorType
    private fun isValidPassword(password: String): Boolean {
        if (password.isEmpty()) {
            i("AuthenticationViewModel.isValidPassword.false.empty")
            _passwordErrorType.value = PasswordErrorType.EMPTY
            return false
        }
        return true
    }

    // First Name field validation
    private var _fNameFieldErrMsg = mutableStateOf<String?>(null)
    var fNameFieldErrMsg: State<String?> = _fNameFieldErrMsg
    private fun isValidFirstName(fName: String): Boolean {
        _fNameFieldErrMsg.value = if (fName.isEmpty()) {
            i("AuthenticationViewModel.isValidFirstName.false.empty")
            "First Name field cannot be empty"
            return false
        } else null
        return true
    }

    // Last Name field validation
    private var _lNameFieldErrMsg = mutableStateOf<String?>(null)
    var lNameFieldErrMsg: State<String?> = _lNameFieldErrMsg
    private fun isValidLastName(lName: String): Boolean {
        _lNameFieldErrMsg.value = if (lName.isEmpty()) {
            i("AuthenticationViewModel.isValidLastName.false.empty")
            "Last Name field cannot be empty"
            return false
        } else null
        return true
    }

}
