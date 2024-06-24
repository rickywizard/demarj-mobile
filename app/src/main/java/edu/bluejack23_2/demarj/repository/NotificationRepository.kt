package edu.bluejack23_2.demarj.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import edu.bluejack23_2.demarj.model.Notification
import edu.bluejack23_2.demarj.model.PreOrder
import edu.bluejack23_2.demarj.model.PreOrderWithStore
import edu.bluejack23_2.demarj.model.User
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

    private fun getStore(preOrderList: List<PreOrder>, liveData: MutableLiveData<List<PreOrderWithStore>>) {
        val preOrderWithStoreList = mutableListOf<PreOrderWithStore>()

        preOrderList.forEach { preOrder ->
            usersDatabase.child(preOrder.po_ownerId!!).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val store = snapshot.getValue(User::class.java)
                    val preOrderWithStore = PreOrderWithStore(preOrder, store!!)

                    preOrderWithStoreList.add(preOrderWithStore)

                    if (preOrderWithStoreList.size == preOrderList.size) {
                        liveData.value = preOrderWithStoreList
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("ERR", "onCancelledStoreId: $error")
                }

            })
        }
    }

    fun getAllPreOrders(): LiveData<List<PreOrderWithStore>> {
        val liveData = MutableLiveData<List<PreOrderWithStore>>()

        poDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val preOrderList = mutableListOf<PreOrder>()

                snapshot.children.forEach {
                    val preOrder = it.getValue(PreOrder::class.java)
                    preOrder?.let {
                        preOrderList.add(preOrder)
                    }
                }

                getStore(preOrderList, liveData)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ERR", "onCancelled: $error")
            }
        })

        return liveData
    }

    fun getAllNotification(): LiveData<List<PreOrderWithStore>> {
        val liveData = MutableLiveData<List<PreOrderWithStore>>()

        poDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val preOrderList = mutableListOf<PreOrder>()

                snapshot.children.forEach {
                    val preOrder = it.getValue(PreOrder::class.java)
                    preOrder?.let {
                        preOrderList.add(preOrder)
                    }
                }

                getStore(preOrderList, liveData)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ERR", "onCancelled: $error")
            }
        })

        return liveData
    }
}