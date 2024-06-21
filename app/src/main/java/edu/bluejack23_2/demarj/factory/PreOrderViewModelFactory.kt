package edu.bluejack23_2.demarj.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.bluejack23_2.demarj.repository.PreOrderRepository
import edu.bluejack23_2.demarj.viewmodel.PreOrderViewModel

class PreOrderViewModelFactory(private val repository: PreOrderRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PreOrderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PreOrderViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}