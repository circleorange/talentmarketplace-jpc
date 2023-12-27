package com.talentmarketplace.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talentmarketplace.model.UserModel
import com.talentmarketplace.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber.i
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor (
    private val userRepository: UserRepository,
): ViewModel () {

    val uid = MutableLiveData<String>()
    val firstName = MutableLiveData<String?>()
    val lastName = MutableLiveData<String?>()
    val email = MutableLiveData<String>()
    val displayName = MutableLiveData<String?>()
    val profilePictureUrl = MutableLiveData<String?>()

    fun getCurrentUserDetails () {
        viewModelScope.launch {
            val user = userRepository.getCurrentUser()

            i("UserViewModel.getCurrentUserDetails().user: $user")

            user?.let {
                uid.value = it.uid
                firstName.value = it.firstName
                lastName.value = it.lastName
                email.value = it.email
                displayName.value = it.username
                profilePictureUrl.value = it.profilePictureUrl
            }
        }
    }

    fun updateUserDetails() {
        val updatedUserDetails = UserModel(
            uid = uid.value ?: return,
            firstName = firstName.value,
            lastName = lastName.value,
            email = email.value ?: return,
            username = displayName.value,
            profilePictureUrl = profilePictureUrl.value,
        )
        i("UserViewModel.updateUserDetails().user: $updatedUserDetails")

        viewModelScope.launch {
            userRepository.updateUser(updatedUserDetails)
        }
    }
}