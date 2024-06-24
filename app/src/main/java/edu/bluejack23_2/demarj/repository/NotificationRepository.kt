package edu.bluejack23_2.demarj.repository

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import edu.bluejack23_2.demarj.model.Notification
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class NotificationRepository {

    private val notifDatabase: DatabaseReference = FirebaseDatabase.getInstance("https://demarj-59046-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("notification")
    private val poDatabase: DatabaseReference = FirebaseDatabase.getInstance("https://demarj-59046-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("pre_orders")
    private val usersDatabase: DatabaseReference = FirebaseDatabase.getInstance("https://demarj-59046-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("users")

    suspend fun addNotification(user_id: String, pre_order_id: String): Result<String> = suspendCoroutine { continuation ->
        val notif_id = notifDatabase.push().key ?: run {
            continuation.resume(Result.failure(Exception("Failed to generate unique ID")))
            return@suspendCoroutine
        }

        Log.d("Notif Repository", notif_id)
        val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val date_time_string = currentDateTime.toString()
        Log.d("Time Notif Repository", date_time_string)
        val notif = Notification (
            notifId = notif_id,
            userId = user_id,
            preOrderId = pre_order_id,
            notifTime = date_time_string
                )

        notifDatabase.child(notif_id).setValue(notif).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Notif Repository", "Add notification successful")
                continuation.resume(Result.success(notif_id))
            } else {
                Log.e("PreOrderRepository", "Add notification failed", task.exception)
                continuation.resume(Result.failure(task.exception ?: Exception("Unknown error occurred")))
            }
        }
    }
}