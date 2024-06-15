package edu.bluejack23_2.demarj.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.bluejack23_2.demarj.repositories.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val userRepository = UserRepository()

    private val _loginResult = MutableLiveData<Result<String>>()
    val loginResult: LiveData<Result<String>> get() = _loginResult

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "^[A-Za-z0-9._%+-]+@gmail\\.com$"
        return email.matches(emailPattern.toRegex())
    }

    fun validateLogin(email: String, password: String){
        if(!email.isNotEmpty()){
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

        _errorMessage.value = null
        loginUser(email, password)
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            val loginResult = userRepository.loginAuth(email, password)
            if (loginResult.isFailure) {
                _errorMessage.postValue(loginResult.exceptionOrNull()?.message)
            } else {
                _loginResult.postValue(loginResult)
            }
        }
    }

}