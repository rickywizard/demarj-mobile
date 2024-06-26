package edu.bluejack23_2.demarj.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bluejack23_2.demarj.model.History
import edu.bluejack23_2.demarj.model.Transaction
import edu.bluejack23_2.demarj.repository.TransactionRepository

class TransactionViewModel: ViewModel() {
    private val repository = TransactionRepository()

    private val _transactionHistory = MutableLiveData<List<History>>()
    val transactionHistory: LiveData<List<History>> get() = _transactionHistory

    fun fetchTransactionHistory(userId: String) {
        _transactionHistory.postValue(emptyList()) // Clear previous data
        repository.getHistoryByUserId(userId).observeForever {
            _transactionHistory.postValue(it)
        }
    }

    fun addTransaction(transaction: Transaction, availableStock: Int, onComplete: (Boolean, String?) -> Unit) {
        if (transaction.poId?.isBlank() == true || transaction.userId?.isBlank() == true) {
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
}