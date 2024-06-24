package edu.bluejack23_2.demarj.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import edu.bluejack23_2.demarj.R
import edu.bluejack23_2.demarj.databinding.ActivityLoginBinding
import edu.bluejack23_2.demarj.factory.LoginViewModelFactory
import edu.bluejack23_2.demarj.viewmodel.LoginViewModel
import java.util.concurrent.Executor

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = LoginViewModelFactory(applicationContext)
        loginViewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)

        binding.toRegisterBttn.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.loginBttn.setOnClickListener{
            val email = binding.emailLogin.text.toString()
            val pass = binding.passwordLogin.text.toString()

            loginViewModel.validateLogin(email, pass)
        }

        loginViewModel.loginResult.observe(this, Observer { result ->
            if (result.isFailure) {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
            }
        })

        loginViewModel.errorMessage.observe(this, Observer { errorMsg ->
            errorMsg?.let {err ->
                if(err != null){
                    Toast.makeText(this, err, Toast.LENGTH_SHORT).show()
                }
            }
        })

        loginViewModel.userData.observe(this, Observer { result ->
            if (result.isSuccess){
                Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            else if (result.isFailure) {
                Toast.makeText(this, "Failed to fetch user data: ${result.exceptionOrNull()?.message}", Toast.LENGTH_SHORT).show()
            }

        })

        loginViewModel.biometricAuthResult.observe(this, Observer { result ->
            result.fold(
                onSuccess = {
                    Toast.makeText(this, "Biometric authentication successful", Toast.LENGTH_SHORT).show()
                    sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                    val loggedIn = sharedPreferences.getString("email", null)
                    if (loggedIn != null) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(this, "No User", Toast.LENGTH_SHORT).show()
                    }
                },
                onFailure = { throwable ->
                    Toast.makeText(this, "Biometric authentication failed: ${throwable.message}", Toast.LENGTH_SHORT).show()
                }
            )
        })

        binding.fingerprintLogin.setOnClickListener {
            if (loginViewModel.canAuthenticateWithBiometrics()) {
                loginViewModel.initBiometricPrompt()
            } else {
                Toast.makeText(this, "Biometric authentication is not available", Toast.LENGTH_SHORT).show()
            }
        }
    }
}