package edu.bluejack23_2.demarj.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bluejack23_2.demarj.model.PreOrder
import edu.bluejack23_2.demarj.model.PreOrderWithStore
import edu.bluejack23_2.demarj.repository.PreOrderRepository

class PreOrderViewModel() : ViewModel() {

    private val repository = PreOrderRepository()
    val preOrdersWithStore: LiveData<List<PreOrderWithStore>> = repository.getAllPreOrders()

    private val _preOrdersByStoreId = MutableLiveData<List<PreOrder>>()
    val preOrdersByStoreId: LiveData<List<PreOrder>> get() = _preOrdersByStoreId

    fun fetchPreOrderByStoreId(storeId: String) {
        repository.getPreOrderById(storeId).observeForever {
            _preOrdersByStoreId.value = it
        }
    }
}