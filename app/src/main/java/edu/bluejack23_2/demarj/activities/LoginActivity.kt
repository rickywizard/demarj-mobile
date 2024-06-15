package edu.bluejack23_2.demarj.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import edu.bluejack23_2.demarj.R
import edu.bluejack23_2.demarj.databinding.ActivityLoginBinding
import edu.bluejack23_2.demarj.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toRegisterBttn.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.loginBttn.setOnClickListener{
            val email = binding.emailLogin.text.toString()
            val pass = binding.passwordLogin.text.toString()

            loginViewModel.validateLogin(email, pass)
        }

        loginViewModel.loginResult.observe(this, Observer { result ->
            if (result.isSuccess) {
                Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
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
    }
}