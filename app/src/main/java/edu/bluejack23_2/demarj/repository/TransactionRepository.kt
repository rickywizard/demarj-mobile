package edu.bluejack23_2.demarj.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import edu.bluejack23_2.demarj.model.History
import edu.bluejack23_2.demarj.model.PreOrder
import edu.bluejack23_2.demarj.model.Transaction
import edu.bluejack23_2.demarj.model.User

class TransactionRepository {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://demarj-59046-default-rtdb.asia-southeast1.firebasedatabase.app")
    private val transactionRef: DatabaseReference = database.getReference("transactions")
    private val preOrderRef: DatabaseReference = database.getReference("pre_orders")
    private val storeRef: DatabaseReference = database.getReference("users")

    fun addTransaction(transaction: Transaction, onComplete: (Boolean) -> Unit) {
        val transactionId = transactionRef.push().key ?: ""
        val transactionWithId = transaction.copy(transactionId = transactionId)
        transactionRef.child(transactionId).setValue(transactionWithId).addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }

    fun getHistoryByUserId(userId: String) : LiveData<List<History>> {
        val liveData = MutableLiveData<List<History>>()

        transactionRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val transactions = snapshot.children.mapNotNull { it.getValue(Transaction::class.java) }
                // Fetch products and stores for each transaction
                fetchProductsAndStores(transactions, liveData)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ERR", "onCancelledHistory: $error")
            }
        })

        return liveData
    }

    private fun fetchProductsAndStores(transactions: List<Transaction>, liveData: MutableLiveData<List<History>>){
        val historyList = mutableListOf<History>()

        transactions.forEach { transaction ->
            preOrderRef.child(transaction.poId!!).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val product = snapshot.getValue(PreOrder::class.java)
                    product?.let {
                        storeRef.child(it.po_ownerId!!).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(storeSnapshot: DataSnapshot) {
                                val store = storeSnapshot.getValue(User::class.java)
                                val history = History(transaction, it, store!!)
                                historyList.add(history)
                                if (historyList.size == transactions.size) {
                                    liveData.value = historyList
                                }
                            }

                            override fun onCancelled(storeError: DatabaseError) {
                                Log.e("ERR", "onCancelledStore: $storeError")
                            }
                        })
                    }
                }

                override fun onCancelled(poError: DatabaseError) {
                    Log.e("ERR", "onCancelledPO: $poError")
                }
            })
        }
    }
}