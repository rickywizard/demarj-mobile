package edu.bluejack23_2.demarj.activities

import android.app.Activity
import android.util.Log
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import edu.bluejack23_2.demarj.databinding.ActivityRegisterBinding
import edu.bluejack23_2.demarj.model.User
import edu.bluejack23_2.demarj.viewmodels.RegisterViewModel
import kotlin.coroutines.resume
import kotlin.math.log

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    //    private lateinit var getContent: ActivityResultLauncher<String>
    private val registerViewModel: RegisterViewModel by viewModels()
    private var profilePictureUri: Uri? = null

    private val database: DatabaseReference = FirebaseDatabase.getInstance("https://demarj-59046-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toLoginBttn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val galeryImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                binding.photoProfileRegister.setImageURI(it)
                profilePictureUri = it
            }
        )

        binding.takePhotoBttn.setOnClickListener {
            galeryImage.launch("image/*")
        }

        binding.cbRole.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.storeNameTextRegister.visibility = View.VISIBLE
                binding.storeNameRegister.visibility = View.VISIBLE
            } else {
                binding.storeNameTextRegister.visibility = View.GONE
                binding.storeNameRegister.visibility = View.GONE
            }
        }

        binding.registerBttn.setOnClickListener {
            val fullname = binding.fullnameRegister.text.toString()
            val email = binding.emailRegister.text.toString()
            val password = binding.passwordRegister.text.toString()
            val phone_number = binding.phoneRegister.text.toString()
            val role = if (binding.cbRole.isChecked) "Store Owner" else "User"
            val store_name =
                if (binding.cbRole.isChecked) binding.storeNameRegister.text.toString() else "-"

            if (profilePictureUri == null) {
                Toast.makeText(this, "Please select a profile picture", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registerViewModel.validateRegister(
                profilePictureUri!!,
                fullname,
                email,
                password,
                phone_number,
                role,
                store_name)
        }

        registerViewModel.registerResult.observe(this, Observer { result ->
            if (result.isSuccess) {
                Toast.makeText(this, "Registration Successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
            }
        })

        registerViewModel.errorMessage.observe(this, Observer { errorMsg ->
            errorMsg?.let { err ->
                if(err != null){
                    Toast.makeText(this, err, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

}
