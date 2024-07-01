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

    private val _totalPrice = MutableLiveData<Int>()
    val totalPrice: LiveData<Int> get() = _totalPrice

    fun fetchTransactionsWithUserByProductId(productId: String) {
        repository.fetchTransactionsWithUserByProductId(productId) { transactionsWithUser ->
            _transactionsWithUser.postValue(transactionsWithUser)
            calculateTotalPrice(transactionsWithUser)
        }
    }

    private fun calculateTotalPrice(transactionsWithUser: List<TransactionWithUser>) {
        var total = 0
        for (transactionWithUser in transactionsWithUser) {
            total += transactionWithUser.transaction.total_price!!
        }
        _totalPrice.postValue(total)
    }

    fun fetchTransactionHistory(userId: String) {
        _transactionHistory.postValue(emptyList()) // Clear previous data
        repository.getHistoryByUserId(userId).observeForever {
            _transactionHistory.postValue(it)
        }
    }

    fun updateTakenStatus(transactionId: String, taken: Boolean, onComplete: (Boolean, String?) -> Unit) {
        repository.updateTakenStatus(transactionId, taken) { isSuccess ->
            onComplete(isSuccess, "Successfully update taken status")
        }
    }

    fun updatePaidStatus(transactionId: String, paid: Boolean, onComplete: (Boolean, String?) -> Unit) {
        repository.updatePaidStatus(transactionId, paid) { isSuccess ->
            onComplete(isSuccess, "Successfully update payment status")
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
        if (ready!= null && ready.before(today)) {
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