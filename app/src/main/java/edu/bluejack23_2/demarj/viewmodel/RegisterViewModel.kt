package edu.bluejack23_2.demarj.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
import edu.bluejack23_2.demarj.model.User
import edu.bluejack23_2.demarj.repositories.UserRepository
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

//    private val userRepository = UserRepository()
//
//    private val _registrationStatus = MutableLiveData<Boolean>()
//    val registrationStatus: LiveData<Boolean> = _registrationStatus
//
//    private val _errorMessage = MutableLiveData<String>()
//    val errorMessage: LiveData<String> = _errorMessage
//
//    fun register(profilePictureUri: Uri, fullname: String, email: String, password: String, phoneNumber: String, role: String, storeName: String) {
//        viewModelScope.launch {
//            val registerResult = userRepository.registerUser(email, password)
//            if (registerResult.isSuccess) {
//                val userId = registerResult.getOrNull() ?: return@launch
//                val uploadResult = userRepository.uploadProfilePicture(userId, profilePictureUri)
//                if (uploadResult.isSuccess) {
//                    val profilePictureUrl = uploadResult.getOrNull() ?: return@launch
//                    val user = User(userId, profilePictureUrl, fullname, email, phoneNumber, role, storeName)
//                    val saveResult = userRepository.saveUserData(user)
//                    _registrationStatus.value = saveResult.isSuccess
//                    if (!saveResult.isSuccess) {
//                        _errorMessage.value = saveResult.exceptionOrNull()?.message
//                    }
//                } else {
//                    _errorMessage.value = uploadResult.exceptionOrNull()?.message
//                    _registrationStatus.value = false
//                }
//            } else {
//                _errorMessage.value = registerResult.exceptionOrNull()?.message
//                _registrationStatus.value = false
//            }
//        }
//    }
}
