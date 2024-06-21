package edu.bluejack23_2.demarj.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.bluejack23_2.demarj.model.PreOrder
import edu.bluejack23_2.demarj.repository.PreOrderRepository
import kotlinx.coroutines.launch

class PreOrderViewModel(private val repository: PreOrderRepository) : ViewModel() {

    private val _preOrders = MutableLiveData<List<PreOrder>>()
    val preOrders: LiveData<List<PreOrder>> get() = _preOrders

    private val _storeNames = MutableLiveData<Map<String, String>>()
    val storeNames: LiveData<Map<String, String>> get() = _storeNames

    fun fetchAllPreOrders() {
        viewModelScope.launch {
            val result = repository.getAllPreOrder()
            result.fold(
                onSuccess = { preOrders ->
                    Log.d("PREORDER", "$preOrders")
                    _preOrders.postValue(preOrders)

                    val storeNamesMap = mutableMapOf<String, String>()
                    for (preOrder in preOrders) {
                        val storeNameResult = repository.getStoreNameByOwnerId(preOrder.po_ownerId!!)
                        storeNameResult.fold(
                            onSuccess = { storeName ->
                                storeNamesMap[preOrder.po_ownerId] = storeName
                            },
                            onFailure = {
                                storeNamesMap[preOrder.po_ownerId] = "Unknown Store"
                            }
                        )
                    }
                    _storeNames.postValue(storeNamesMap)
                },
                onFailure = { error ->
                    Log.e("ERROR FETCHING", error.message.toString())
                }
            )
        }
    }

}