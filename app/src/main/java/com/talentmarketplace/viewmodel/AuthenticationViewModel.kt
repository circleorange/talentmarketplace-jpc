package com.talentmarketplace.viewmodel

import android.content.Intent
import android.content.IntentSender
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talentmarketplace.model.FirestoreUserModel
import com.talentmarketplace.model.UserModel
import com.talentmarketplace.model.authentication.AuthState
import com.talentmarketplace.model.authentication.EmailErrorType
import com.talentmarketplace.model.authentication.PasswordErrorType
import com.talentmarketplace.repository.auth.BasicAuthRepository
import com.talentmarketplace.repository.auth.GoogleAuthRepository
import com.talentmarketplace.repository.auth.firebase.GoogleAuthState
import com.talentmarketplace.repository.firestore.UserFirestoreRepository
import com.talentmarketplace.utils.SignInMethodManager
import com.talentmarketplace.view.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber.i
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val basicAuthRepository: BasicAuthRepository,
    private val googleAuthRepository: GoogleAuthRepository,
    private val userRepository: UserFirestoreRepository,
    private val signInMethodManager: SignInMethodManager,
): ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState = _authState.asStateFlow()

    val firstName = mutableStateOf("")
    val lastName = mutableStateOf("")
    val email = mutableStateOf("")
    val password = mutableStateOf("")

    private val _signInEvent = MutableSharedFlow<String>()
    val signInEvent = _signInEvent.asSharedFlow()
    fun signIn(email: String, password: String) {
        viewModelScope.launch {

            // Validate fields not empty
            val validEmail = isValidEmail(email)
            val validPassword = isValidPassword(password)

            // Exit function for invalid input
            if (!validEmail || !validPassword) {
                i("AuthenticationViewModel.signIn.fail.empty")
                return@launch
            }
            _authState.value = AuthState.Loading
            val result = basicAuthRepository.signIn(email, password)

            if (result.isSuccess) {
                val user = result.getOrNull()!!
                _authState.value = AuthState.Authenticated(user)

                signInMethodManager.setSignInMethod(SignInMethodManager.BASIC)

                _signInEvent.emit(Routes.Job.List.route)
                i("AuthenticationViewModel.signIn.value: ${_authState.value}")
            }
            else {
                _authState.value = AuthState.InvalidAuthentication
                i("AuthenticationViewModel.signIn.fail.invalid")
            }
        }
    }

    fun signIn() {
        viewModelScope.launch {
            _signInEvent.emit(Routes.Auth.SignIn.route)
        }
    }

    private val _signUpEvent = MutableSharedFlow<String>()
    val signUpEvent = _signUpEvent.asSharedFlow()
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
            val result = basicAuthRepository.signUp(firstName, lastName, email, password)

            if (result.isSuccess) {
                val user = result.getOrNull()!!
                _authState.value = AuthState.Authenticated(user)

                signInMethodManager.setSignInMethod(SignInMethodManager.BASIC)

                _signUpEvent.emit(Routes.Job.List.route)
                i("AuthenticationViewModel.signUp.success: ${_authState.value}")
            }
            else {
                _emailErrorType.value = EmailErrorType.TAKEN
                _authState.value = AuthState.InvalidAuthentication
            }
        }
    }

    fun signUp() {
        viewModelScope.launch {
            _signUpEvent.emit(Routes.Auth.SignUp.route)
        }
    }

    private val _signOutEvent = MutableSharedFlow<String>()
    val signOutEvent = _signOutEvent.asSharedFlow()
    fun signOut() {
        viewModelScope.launch {
            _signOutEvent.emit(Routes.Auth.SignIn.route)
        }
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

    private val _googleAuthState = MutableStateFlow(GoogleAuthState())
    val googleAuthState = _googleAuthState.asStateFlow()

    fun resetState() {
        _googleAuthState.update { GoogleAuthState() }
    }

    // This event will be observed by the UI to launch the Google Sign-In process
    private val _googleSignInRequest = MutableSharedFlow<IntentSender?>()
    val googleSignInRequest = _googleSignInRequest.asSharedFlow()

    fun initiateGoogleSignIn() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val intentSender = googleAuthRepository.signIn()
                _googleSignInRequest.emit(intentSender)
            } catch (e: Exception) {
                _authState.value = AuthState.InvalidAuthentication
            }
        }
    }

    // Helper to convert FirebaseUser to UserModel
    private fun UserModel.toFirestoreUser(): FirestoreUserModel {
        return FirestoreUserModel(
            uid = this.uid,
            username = this.username,
            email = this.email,
            profilePictureUrl = profilePictureUrl,
            firstName = this.firstName,
            lastName = this.lastName,
        )
    }

    fun processGoogleSignInResult(data: Intent?) {
        viewModelScope.launch {
            val result = data?.let { googleAuthRepository.signInWithIntent(it) }
            if (result == null) {
                _authState.value = AuthState.InvalidAuthentication
                return@launch
            }
            if (result.data == null) {
                _authState.value = AuthState.InvalidAuthentication
                return@launch
            }
            _authState.value = result.data.let { AuthState.Authenticated(it) }
            i("AuthenticationViewModel.processGoogleSignInResult: ${_authState.value}")

            // val firebaseUser = result.data.toFirestoreUser()
            // userRepository.createUser(firebaseUser)
            userRepository.createUser(result.data)

            signInMethodManager.setSignInMethod(SignInMethodManager.GOOGLE)

            _signInEvent.emit(Routes.Job.List.route)
        }
    }

    // Call this method to reset the sign-in request event after launching
    fun resetGoogleSignInRequest() {
        viewModelScope.launch {
            _googleSignInRequest.emit(null)
        }
    }

}
