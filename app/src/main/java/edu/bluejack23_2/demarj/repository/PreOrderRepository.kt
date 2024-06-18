package edu.bluejack23_2.demarj.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import edu.bluejack23_2.demarj.model.PreOrder
import edu.bluejack23_2.demarj.model.User
import java.time.LocalDate
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PreOrderRepository {

    private val database: DatabaseReference = FirebaseDatabase.getInstance("https://demarj-59046-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("pre_orders")
    private val storage = FirebaseStorage.getInstance().reference

    suspend fun getPreOrderById(poId: String): Result<PreOrder> = suspendCoroutine { continuation ->
        database.child(poId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val preOrder = snapshot.getValue(PreOrder::class.java)
                if (preOrder != null) {
                    continuation.resume(Result.success(preOrder))
                } else {
                    continuation.resume(Result.failure(Exception("PreOrder not found")))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                continuation.resume(Result.failure(error.toException()))
            }
        })
    }

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
            po_ownerId = po_ownerId
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
}