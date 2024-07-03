package edu.bluejack23_2.demarj.viewmodel

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.bluejack23_2.demarj.model.User
import edu.bluejack23_2.demarj.repositories.UserRepository
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

class LoginViewModel(private val context: Context) : ViewModel() {

    private val userRepository = UserRepository()

    private val _loginResult = MutableLiveData<Result<String>>()
    val loginResult: LiveData<Result<String>> get() = _loginResult

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _userData = MutableLiveData<Result<User>>()
    val userData: LiveData<Result<User>> get() = _userData

    private val _biometricPromptInfo = MutableLiveData<BiometricPrompt.PromptInfo>()
    val biometricPromptInfo: LiveData<BiometricPrompt.PromptInfo> get() = _biometricPromptInfo

    private val _biometricAuthResult = MutableLiveData<Result<Boolean>>()
    val biometricAuthResult: LiveData<Result<Boolean>> get() = _biometricAuthResult

    fun initBiometricPrompt(activity: AppCompatActivity) {
        val executor: Executor = ContextCompat.getMainExecutor(context)
        val biometricPrompt = BiometricPrompt(activity, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                _biometricAuthResult.value = Result.failure(Throwable(errString.toString()))
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                _biometricAuthResult.value = Result.success(true)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                _biometricAuthResult.value = Result.failure(Throwable("Authentication failed"))
            }
        })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for DEmaRJ")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Cancel")
            .build()

        _biometricPromptInfo.value = promptInfo
        biometricPrompt.authenticate(promptInfo)
    }

    fun canAuthenticateWithBiometrics(): Boolean {
        val biometricManager = BiometricManager.from(context)
        return biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS
    }

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

    private fun saveUserDataToPrefs(user: User) {
        Log.d("SHARED PREFERENCES", "SIMPAN")
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("user_id", user.userId)
        editor.putString("email", user.email)
        editor.putString("fullname", user.fullname)
        editor.putString("phone_number", user.phone_number)
        editor.putString("role", user.role)
        editor.putString("store_name", user.store_name)
        editor.putString("profile_picture", user.profile_picture)
        editor.apply()
    }

    fun fetchUserData(userId: String){
        viewModelScope.launch {
            val userResult = userRepository.getUserData(userId)
            if (userResult.isSuccess) {
                val user = userResult.getOrNull()
                user?.let {
                    saveUserDataToPrefs(it)
                    _userData.postValue(userResult)
                }
            } else {
                _userData.postValue(userResult)
            }
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            val loginResult = userRepository.loginAuth(email, password)
            if (loginResult.isFailure) {
                _errorMessage.value = "Invalid credentials"
            } else {
                val userId = loginResult.getOrNull() ?: return@launch
                fetchUserData(userId)
                _loginResult.postValue(loginResult)
            }
        }
    }

}