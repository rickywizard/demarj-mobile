package edu.bluejack23_2.demarj.viewmodel

import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {

    private val _registrationStatus = MutableLiveData<Boolean>()
    val registrationStatus: LiveData<Boolean> get() = _registrationStatus

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val userRepository = UserRepository()

    fun register(fullname: String, email: String, password: String, phoneNumber: String, storeName: String) {
        val user = User(fullname, email, phoneNumber, storeName)
        userRepository.registerUser(user, password) { success, message ->
            _registrationStatus.value = success
            if (!success) {
                _errorMessage.value = message
            }
        }
    }

}