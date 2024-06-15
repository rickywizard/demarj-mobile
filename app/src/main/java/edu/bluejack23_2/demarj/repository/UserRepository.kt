package edu.bluejack23_2.demarj.repositories

import android.util.Log
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import edu.bluejack23_2.demarj.model.User
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
    private val storage = FirebaseStorage.getInstance().reference

    suspend fun registerAuth(email: String, password: String): Result<String> = suspendCoroutine { continuation ->
        Log.d("UserRepository", "Attempting to register auth with email: $email")
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                Log.d("UserRepository", "Auth registration successful, userId: $userId")
                continuation.resume(Result.success(userId))
            } else {
                Log.e("UserRepository", "Auth registration failed", task.exception)
                continuation.resume(Result.failure(task.exception ?: Exception("Unknown error occurred")))
            }
        }
    }

    suspend fun uploadProfilePicture(userId: String, uri: Uri): Result<String> = suspendCoroutine { continuation ->
        Log.d("UserRepository", "Uploading profile picture for userId: $userId, uri: $uri")
        val profilePicRef = storage.child("profile_pictures/$userId/${uri.lastPathSegment}")
        profilePicRef.putFile(uri).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("UserRepository", "Profile picture upload successful")
                profilePicRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    Log.d("UserRepository", "Download URL: $downloadUrl")
                    continuation.resume(Result.success(downloadUrl.toString()))
                }
            } else {
                Log.e("UserRepository", "Profile picture upload failed", task.exception)
                continuation.resume(Result.failure(task.exception ?: Exception("Unknown error occurred")))
            }
        }
    }

    suspend fun registerUser(userId: String, profpict: String, fullname: String, email: String, phone_number: String, role: String, store_name: String): Result<String> = suspendCoroutine {continuation ->
        Log.d("UserRepository", "Registering user: $userId")
        val user = User (
            userId = userId,
            profile_picture = profpict,
            fullname = fullname,
            email = email,
            phone_number = phone_number,
            role = role,
            store_name = store_name
        )
        database.child(userId).setValue(user).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("UserRepository", "User registration successful")
                continuation.resume(Result.success(userId))
            } else {
                Log.e("UserRepository", "User registration failed", task.exception)
                continuation.resume(Result.failure(task.exception ?: Exception("Unknown error occurred")))
            }
        }
    }
}