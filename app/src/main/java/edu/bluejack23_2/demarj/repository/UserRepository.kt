package edu.bluejack23_2.demarj.repositories

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import edu.bluejack23_2.demarj.model.User
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserRepository {

//    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
//    private val database = FirebaseDatabase.getInstance().reference
//    private val storage = FirebaseStorage.getInstance().reference
//
//    suspend fun registerUser(email: String, password: String): Result<String> = suspendCoroutine { continuation ->
//        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
//                continuation.resume(Result.success(userId))
//            } else {
//                continuation.resume(Result.failure(task.exception ?: Exception("Unknown error occurred")))
//            }
//        }
//    }
//
//    suspend fun uploadProfilePicture(userId: String, uri: Uri): Result<String> = suspendCoroutine { continuation ->
//        val profilePicRef = storage.child("profile_pictures/$userId.jpg")
//        profilePicRef.putFile(uri).addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                profilePicRef.downloadUrl.addOnSuccessListener { downloadUrl ->
//                    continuation.resume(Result.success(downloadUrl.toString()))
//                }
//            } else {
//                continuation.resume(Result.failure(task.exception ?: Exception("Unknown error occurred")))
//            }
//        }
//    }
//
//    suspend fun saveUserData(user: User): Result<Void> = suspendCoroutine { continuation ->
//        database.child("users").child(user.userId).setValue(user).addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                continuation.resume(Result.success(null))
//            } else {
//                continuation.resume(Result.failure(task.exception ?: Exception("Unknown error occurred")))
//            }
//        }
//    }
}
