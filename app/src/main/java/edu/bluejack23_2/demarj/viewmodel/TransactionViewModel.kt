package edu.bluejack23_2.demarj.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bluejack23_2.demarj.model.History
import edu.bluejack23_2.demarj.model.Transaction
import edu.bluejack23_2.demarj.model.TransactionWithUser
import edu.bluejack23_2.demarj.repository.TransactionRepository
import java.text.SimpleDateFormat
import java.util.*

class TransactionViewModel: ViewModel() {
    private val repository = TransactionRepository()

    private val _transactionHistory = MutableLiveData<List<History>>()
    val transactionHistory: LiveData<List<History>> get() = _transactionHistory

    private val _deleteResult = MutableLiveData<Boolean>()
    val deleteResult: LiveData<Boolean> get() = _deleteResult

    private val _uploadResult = MutableLiveData<Boolean>()
    val uploadResult: LiveData<Boolean> get() = _uploadResult

    private val _transactionsWithUser = MutableLiveData<List<TransactionWithUser>>()
    val transactionsWithUser: LiveData<List<TransactionWithUser>> get() = _transactionsWithUser

    fun fetchTransactionsWithUserByProductId(productId: String) {
        repository.fetchTransactionsWithUserByProductId(productId) { transactionsWithUser ->
            _transactionsWithUser.postValue(transactionsWithUser)
        }
    }

    fun fetchTransactionHistory(userId: String) {
        _transactionHistory.postValue(emptyList()) // Clear previous data
        repository.getHistoryByUserId(userId).observeForever {
            _transactionHistory.postValue(it)
        }
    }

    fun addTransaction(role: String, transaction: Transaction, availableStock: Int, onComplete: (Boolean, String?) -> Unit) {
        if (role != "User") {
            onComplete(false, "You have to be User to order!")
            return
        }
        else if (transaction.poId?.isBlank() == true || transaction.userId?.isBlank() == true) {
            onComplete(false, "All fields must be filled out correctly")
            return
        }
        else if (transaction.quantity!! <= 0) {
            onComplete(false, "Quantity cannot be less then zero")
            return
        }
        else if (transaction.total_price!! <= 0) {
            onComplete(false, "Total price count error")
            return
        }
        else if (transaction.quantity > availableStock) {
            onComplete(false, "Quantity exceeds available stock")
            return
        }
        else {
            repository.addTransaction(transaction) { isSuccess ->
                onComplete(isSuccess, null)
            }
        }
    }

    fun deleteTransaction(transactionId: String, readyDate: String) {
        val today = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val ready = dateFormat.parse(readyDate)
        if (ready!= null && ready.after(today)) {
            repository.deleteTransaction(transactionId) { success ->
                _deleteResult.postValue(success)
            }
        }
    }

    fun uploadProofImage(transactionId: String, imageUri: Uri) {
        repository.uploadProofImage(transactionId, imageUri) { success ->
            _uploadResult.postValue(success)
        }
    }
}