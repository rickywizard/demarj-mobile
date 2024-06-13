package edu.bluejack23_2.demarj.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
//import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import edu.bluejack23_2.demarj.databinding.ActivityRegisterBinding
import edu.bluejack23_2.demarj.viewmodels.RegisterViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
//    private val registerViewModel: RegisterViewModel by viewModels()
    private var profilePictureUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toLoginBttn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.takePhotoBttn.setOnClickListener {
            selectProfilePicture()
        }

//        binding.registerBttn.setOnClickListener {
//            val fullname = binding.fullnameRegister.text.toString()
//            val email = binding.emailRegister.text.toString()
//            val password = binding.passwordRegister.text.toString()
//            val phoneNumber = binding.phoneRegister.text.toString()
//            val role = binding.roleRegister.text.toString()
//            val storeName = binding.storeNameRegister.text.toString()
//
//            if (profilePictureUri != null && fullname.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && phoneNumber.isNotEmpty() && role.isNotEmpty() && storeName.isNotEmpty()) {
//                registerViewModel.register(profilePictureUri!!, fullname, email, password, phoneNumber, role, storeName)
//            } else {
//                Toast.makeText(this, "All fields must not be empty and profile picture must be selected!", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        registerViewModel.registrationStatus.observe(this, Observer { status ->
//            if (status) {
//                val intent = Intent(this, LoginActivity::class.java)
//                startActivity(intent)
//            } else {
//                Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
//            }
//        })
//
//        registerViewModel.errorMessage.observe(this, Observer { message ->
//            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//        })
    }

    private val selectImageResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            profilePictureUri = result.data!!.data
            profilePictureUri?.let {
                Glide.with(this).load(it).into(binding.photoProfileRegister)
            }
        }
    }

    private fun selectProfilePicture() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        selectImageResultLauncher.launch(intent)
    }
}
