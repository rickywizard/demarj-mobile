package edu.bluejack23_2.demarj.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import edu.bluejack23_2.demarj.model.PreOrderWithStore
import edu.bluejack23_2.demarj.repository.PreOrderRepository

class PreOrderViewModel() : ViewModel() {

    private val repository = PreOrderRepository()
    val preOrdersWithStore: LiveData<List<PreOrderWithStore>> = repository.getAllPreOrders()

}