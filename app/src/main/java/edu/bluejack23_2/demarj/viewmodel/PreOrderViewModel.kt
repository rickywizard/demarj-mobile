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

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _deleteStatus = MutableLiveData<Boolean>()
    val deleteStatus: LiveData<Boolean> get() = _deleteStatus

    fun deleteProduct(poId: String) {
        repository.deletePO(poId, {
            _deleteStatus.value = true
        }, { error ->
            Log.e("PreOrderViewModel", "Delete failed: $error")
            _deleteStatus.value = false
        })
    }

    fun updatePreOrder(preOrder: PreOrder, imageUri: Uri?) {
        if (preOrder.po_name == null || preOrder.po_name.isBlank()) {
            _errorMessage.value = "Pre Order name cannot be empty"
            return
        }
        if (preOrder.po_desc == null || preOrder.po_desc.isBlank()) {
            _errorMessage.value = "Pre Order description cannot be empty"
            return
        }
        if (preOrder.po_price == null || preOrder.po_price <= 0) {
            _errorMessage.value = "Pre Order name cannot be empty"
            return
        }
        if (preOrder.po_end_date == null || preOrder.po_end_date.isBlank()) {
            _errorMessage.value = "Pre Order end date cannot be empty"
            return
        }
        if (preOrder.po_ready_date == null || preOrder.po_ready_date.isBlank()) {
            _errorMessage.value = "Pre Order end date cannot be empty"
            return
        }
        if (preOrder.po_stock == null || preOrder.po_stock <= 0) {
            _errorMessage.value = "Pre Order end date cannot be empty"
            return
        }

        if (imageUri == null) {
            repository.updatePreOrder(preOrder, {
                _updateStatus.value = true
            }, { error ->
                Log.e("ProductViewModel", "Update failed: $error")
                _updateStatus.value = false
            })
        }
        else {
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
    }

    fun fetchPreOrderByStoreId(storeId: String) {
        _preOrdersByStoreId.postValue(emptyList())
        repository.getPreOrderById(storeId).observeForever {
            _preOrdersByStoreId.value = it
        }
    }
}