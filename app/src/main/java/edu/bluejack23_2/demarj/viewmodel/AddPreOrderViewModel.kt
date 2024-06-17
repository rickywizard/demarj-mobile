package edu.bluejack23_2.demarj.viewmodel

import android.content.SharedPreferences
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.bluejack23_2.demarj.repository.PreOrderRepository
import kotlinx.coroutines.launch
import java.time.LocalDate

class AddPreOrderViewModel : ViewModel() {

    private val preOrderRepository = PreOrderRepository()
    private lateinit var sharedPreferences: SharedPreferences

    private val _preOrderResult = MutableLiveData<Result<String>>()
    val preOrderResult: LiveData<Result<String>> get() = _preOrderResult

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun validateAddPreOrder(po_img: Uri, po_name: String, po_desc: String, po_price: Int, po_large_price: Int, po_end_date: LocalDate, po_ready_date: LocalDate, po_stock: Int, po_ownerId: String){
        if(!po_name.isNotEmpty()){
            _errorMessage.value = "Pre order name cannot be empty"
            return
        }
        else if(!po_desc.isNotEmpty()){
            _errorMessage.value = "Pre order description cannot be empty"
            return
        }
        else if(po_price <= 0){
            _errorMessage.value = "Pre order price must be greater than 0"
            return
        }
        else if(po_end_date == null){
            _errorMessage.value = "Pre order end date cannot be empty"
            return
        }
        else if(po_end_date.isBefore(LocalDate.now())){
            _errorMessage.value = "Pre order end date cannot be in the past"
            return
        }
        else if(po_ready_date == null){
            _errorMessage.value = "Pre order ready date cannot be empty"
            return
        }
        else if(po_ready_date.isBefore(LocalDate.now())){
            _errorMessage.value = "Pre order ready date cannot be in the past"
            return
        }
        else if(po_ready_date.isBefore(po_end_date)){
            _errorMessage.value = "Pre order ready date cannot be before pre order end date"
            return
        }
        else if(po_stock <= 0){
            _errorMessage.value = "Pre order stock must be greater than 0"
            return
        }

        _errorMessage.value = null
        addPreOrder(po_img, po_name, po_desc, po_price, po_large_price, po_end_date, po_ready_date, po_stock, po_ownerId)

    }

    fun addPreOrder(po_img: Uri, po_name: String, po_desc: String, po_price: Int, po_large_price: Int, po_end_date: LocalDate, po_ready_date: LocalDate, po_stock: Int, po_ownerId: String) {
        viewModelScope.launch {
            val userId = sharedPreferences.getString("user_id", "") ?: run {
                _errorMessage.postValue("User ID is null")
                return@launch
            }

            val uploadResult = preOrderRepository.uploadPreOrderImg(userId, po_img)
            if (uploadResult.isFailure) {
                _preOrderResult.postValue(uploadResult)
                return@launch
            }
            val poImgUrl = uploadResult.getOrNull() ?: return@launch

            val preOrderResult = preOrderRepository.addPreOrder(poImgUrl, po_name, po_desc, po_price, po_large_price, po_end_date, po_ready_date, po_stock, po_ownerId)
            _preOrderResult.postValue(preOrderResult)
        }
    }

}