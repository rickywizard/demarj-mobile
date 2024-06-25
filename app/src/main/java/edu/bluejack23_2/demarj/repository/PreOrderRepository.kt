package edu.bluejack23_2.demarj.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import edu.bluejack23_2.demarj.model.PreOrder
import edu.bluejack23_2.demarj.model.PreOrderWithStore
import edu.bluejack23_2.demarj.model.User
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PreOrderRepository {

    private val database: DatabaseReference = FirebaseDatabase.getInstance("https://demarj-59046-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("pre_orders")
    private val usersDatabase: DatabaseReference = FirebaseDatabase.getInstance("https://demarj-59046-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("users")
    private val storage = FirebaseStorage.getInstance().reference

    suspend fun updatePreOrder(preOrder: PreOrder): Result<String> = suspendCoroutine { continuation ->
        database.child(preOrder.poId!!).setValue(preOrder).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                continuation.resume(Result.success(preOrder.poId))
            } else {
                continuation.resume(Result.failure(task.exception ?: Exception("Unknown error occurred")))
            }
        }
    }

    suspend fun uploadPreOrderImg(userId: String, uri: Uri): Result<String> = suspendCoroutine { continuation ->
        Log.d("PreOrderRepository", "Uploading pre order img for userId: $userId, uri: $uri")
        val profilePicRef = storage.child("pre_order_img/$userId/${uri.lastPathSegment}")
        profilePicRef.putFile(uri).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("PreOrderRepository", "Pre order img upload successful")
                profilePicRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    Log.d("PreOrderRepository", "Download URL: $downloadUrl")
                    continuation.resume(Result.success(downloadUrl.toString()))
                }
            } else {
                Log.e("PreOrderRepository", "Pre order img upload failed", task.exception)
                continuation.resume(Result.failure(task.exception ?: Exception("Unknown error occurred")))
            }
        }
    }

    suspend fun addPreOrder(po_img: String, po_name: String, po_desc: String, po_price: Int, po_large_price: Int, po_end_date: String, po_ready_date: String, po_stock: Int, po_ownerId: String): Result<String> = suspendCoroutine {continuation ->
        val poId = database.push().key ?: run {
            continuation.resume(Result.failure(Exception("Failed to generate unique ID")))
            return@suspendCoroutine
        }
        Log.d("PO Repository", poId)
        val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val date_time_string = currentDateTime.toString()
        Log.d("Time PO Repository", date_time_string)
        val po = PreOrder (
            poId = poId,
            po_img = po_img,
            po_name = po_name,
            po_desc = po_desc,
            po_price = po_price,
            po_large_price = po_large_price,
            po_end_date = po_end_date,
            po_ready_date = po_ready_date,
            po_stock = po_stock,
            po_ownerId = po_ownerId,
            po_created_at = date_time_string
                )

        database.child(poId).setValue(po).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("PreOrderRepository", "Add pre order successful")
                continuation.resume(Result.success(poId))
            } else {
                Log.e("PreOrderRepository", "Add pre order failed", task.exception)
                continuation.resume(Result.failure(task.exception ?: Exception("Unknown error occurred")))
            }
        }
    }

    fun getAllPreOrders(): LiveData<List<PreOrderWithStore>> {
        val liveData = MutableLiveData<List<PreOrderWithStore>>()

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val preOrderList = mutableListOf<PreOrder>()

                snapshot.children.forEach {
                    val preOrder = it.getValue(PreOrder::class.java)
                    preOrder?.let {
                        preOrderList.add(preOrder)
                    }
                }

                getStoreNames(preOrderList, liveData)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ERR", "onCancelled: $error")
            }
        })

        return liveData
    }

    private fun getStoreNames(preOrderList: List<PreOrder>, liveData: MutableLiveData<List<PreOrderWithStore>>) {
        val preOrderWithStoreList = mutableListOf<PreOrderWithStore>()

        preOrderList.forEach { preOrder ->
            usersDatabase.child(preOrder.po_ownerId!!).addListenerForSingleValueEvent(object : ValueEventListener {
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

    fun getPreOrderById(storeId: String): LiveData<List<PreOrder>> {
        val liveData = MutableLiveData<List<PreOrder>>()

        database.orderByChild("po_ownerId").equalTo(storeId).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val preOrderList = mutableListOf<PreOrder>()

                snapshot.children.forEach {
                    val preOrder = it.getValue(PreOrder::class.java)
                    preOrder?.let {
                        preOrderList.add(preOrder)
                    }

                    liveData.value = preOrderList
                }

//                Log.d("PObySTORE", "onDataChange: $preOrderList")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ERR", "onCancelledMyPO: $error")
            }

        })

        return liveData
    }
}