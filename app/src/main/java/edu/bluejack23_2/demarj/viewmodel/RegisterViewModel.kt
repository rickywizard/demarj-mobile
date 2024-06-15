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

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "^[A-Za-z0-9._%+-]+@gmail\\.com$"
        return email.matches(emailPattern.toRegex())
    }

    private fun isValidPassword(password: String): Boolean {
        val uppercasePattern = ".*[A-Z].*"
        val lowercasePattern = ".*[a-z].*"
        val numberPattern = ".*[0-9].*"

        return password.matches(uppercasePattern.toRegex()) &&
                password.matches(lowercasePattern.toRegex()) &&
                password.matches(numberPattern.toRegex())
    }

    fun validateRegister(profileUri: Uri, fullname: String, email: String, password: String, phone_number: String, role: String, store_name: String){
        if (!fullname.isNotEmpty()) {
            _errorMessage.value = "Fullname cannot be empty"
            return
        }
        else if (!email.isNotEmpty()) {
            _errorMessage.value = "Email cannot be empty"
            return
        }
        else if (!isValidEmail(email)) {
            _errorMessage.value = "Invalid email. Domain must be gmail.com"
            return
        }
        else if (!password.isNotEmpty()){
            _errorMessage.value = "Password cannot be empty"
            return
        }
        else if (!isValidPassword(password)) {
            _errorMessage.value = "Password must include uppercase, lowercase, and number"
            return
        }
        else if (!phone_number.isNotEmpty()){
            _errorMessage.value = "Phone number cannot be empty"
            return
        }
        else if (role.equals("Store Owner") && !store_name.isNotEmpty()){
            _errorMessage.value = "Store name cannot be empty"
            return
        }
        else {
            _errorMessage.value = null
            registerUser(
                profileUri,
                fullname,
                email,
                password,
                phone_number,
                role,
                store_name
            )
        }
    }

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

            val registerUserResult = userRepository.registerUser(userId, profilePictureUrl, fullname, email, phone_number, role, store_name)
            _registerResult.postValue(registerUserResult)
        }
    }
}
