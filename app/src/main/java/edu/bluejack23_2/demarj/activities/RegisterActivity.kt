package edu.bluejack23_2.demarj.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import edu.bluejack23_2.demarj.R
import edu.bluejack23_2.demarj.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding:ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.toLoginBttn.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.registerBttn.setOnClickListener{
            val fullname = binding.fullnameRegister.text
            val email = binding.emailRegister.text.toString()
            val password = binding.passwordRegister.text.toString()
            val phone_number = binding.phoneRegister.text.toString()
            val store_name = binding.storeNameRegister.text

            if(fullname.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && phone_number.isNotEmpty() && store_name.isNotEmpty()){
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener({
                    if(it.isSuccessful){
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                })
            }
            else {
                Toast.makeText(this, "All fields must not be empty !!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}