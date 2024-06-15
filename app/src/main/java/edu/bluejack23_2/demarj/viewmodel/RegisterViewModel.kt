package edu.bluejack23_2.demarj.viewmodels

import android.util.Log
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.bluejack23_2.demarj.repositories.UserRepository
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val userRepository = UserRepository()

    private val _registerResult = MutableLiveData<Result<String>>()
    val registerResult: LiveData<Result<String>> get() = _registerResult

    fun registerUser(profileUri: Uri, fullname: String, email: String, password: String, phone_number: String, role: String, store_name: String) {
        viewModelScope.launch {
            val registerAuthResult = userRepository.registerAuth(email, password)
            if (registerAuthResult.isFailure) {
                _registerResult.postValue(registerAuthResult)
                return@launch
            }

            val userId = registerAuthResult.getOrNull() ?: return@launch

            val uploadResult = userRepository.uploadProfilePicture(userId, profileUri)
            if (uploadResult.isFailure) {
                _registerResult.postValue(uploadResult)
                return@launch
            }


            val profilePictureUrl = uploadResult.getOrNull() ?: return@launch
            Log.d("RegisterViewModel", "uploadProfilePicture successful, profilePictureUrl: $profilePictureUrl")

            val registerUserResult = userRepository.registerUser(userId, profilePictureUrl, fullname, email, phone_number, role, store_name)
            if (registerUserResult.isFailure) {
                Log.e("RegisterViewModel", "registerUser failed", registerUserResult.exceptionOrNull())
            } else {
                Log.d("RegisterViewModel", "registerUser successful")
            }
            _registerResult.postValue(registerUserResult)
        }
    }
}
