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
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                continuation.resume(Result.success(userId))
            } else {
                continuation.resume(Result.failure(task.exception ?: Exception("Unknown error occurred")))
            }
        }
    }

    suspend fun uploadProfilePicture(userId: String, uri: Uri): Result<String> = suspendCoroutine { continuation ->
        val profilePicRef = storage.child("profile_pictures/$userId/${uri.lastPathSegment}")
        profilePicRef.putFile(uri).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                profilePicRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    continuation.resume(Result.success(downloadUrl.toString()))
                }
            } else {
                continuation.resume(Result.failure(task.exception ?: Exception("Unknown error occurred")))
            }
        }
    }

    suspend fun registerUser(userId: String, profpict: String, fullname: String, email: String, phone_number: String, role: String, store_name: String): Result<String> = suspendCoroutine {continuation ->
        Log.d("UserRepository", "Starting registerUser for userId: $userId")
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
                Log.d("Database", "User data inserted successfully")
                continuation.resume(Result.success(userId))
            } else {
                Log.e("Database", "Failed to insert user data", task.exception)
                continuation.resume(Result.failure(task.exception ?: Exception("Unknown error occurred")))
            }
        }
    }
}
