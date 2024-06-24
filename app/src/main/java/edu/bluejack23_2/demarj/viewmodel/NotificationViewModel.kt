package edu.bluejack23_2.demarj.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.bluejack23_2.demarj.repository.NotificationRepository
import kotlinx.coroutines.launch

class NotificationViewModel: ViewModel() {

    private val notifRepository = NotificationRepository()

    private val _notifResult = MutableLiveData<Result<String>>()
    val notifResult: LiveData<Result<String>> get() = _notifResult

    fun addNotification(user_id: String, pre_order_id: String){
        viewModelScope.launch {
            val notifResult = notifRepository.addNotification(user_id, pre_order_id)
            _notifResult.postValue(notifResult)
        }
    }

}