package edu.bluejack23_2.demarj.viewmodel

import android.net.Uri
import android.util.Log
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

    private val _updateStatus = MutableLiveData<Boolean>()
    val updateStatus: LiveData<Boolean> get() = _updateStatus

    fun updateProduct(preOrder: PreOrder, imageUri: Uri) {
        repository.uploadPOImage(preOrder.poId!!, imageUri, { imageUrl ->
            val updatedPO = preOrder.copy(po_img = imageUrl)
            repository.updatePreOrder(updatedPO, {
                _updateStatus.value = true
            }, { error ->
                Log.e("ProductViewModel", "Update failed: $error")
                _updateStatus.value = false
            })
        }, { exception ->
            Log.e("ProductViewModel", "Image upload failed: $exception")
            _updateStatus.value = false
        })
    }

    fun fetchPreOrderByStoreId(storeId: String) {
        repository.getPreOrderById(storeId).observeForever {
            _preOrdersByStoreId.value = it
        }
    }
}