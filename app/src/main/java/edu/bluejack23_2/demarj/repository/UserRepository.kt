package edu.bluejack23_2.demarj.repository

class UserRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val databaseReference = FirebaseDatabase.getInstance().reference

    fun registerUser(user: User, password: String, onComplete: (Boolean, String?) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(user.email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = firebaseAuth.currentUser?.uid ?: return@addOnCompleteListener
                databaseReference.child("users").child(userId).setValue(user).addOnCompleteListener { dbTask ->
                    onComplete(dbTask.isSuccessful, dbTask.exception?.message)
                }
            } else {
                onComplete(false, task.exception?.message)
            }
        }
    }

}