package com.kashifshahazad.smartagriassistant

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeFragmentViewModel : ViewModel() {
    val cropIssueRepository = CropIssueRepository()

    val failureMessage = MutableStateFlow<String?>(null)
    val dataa = MutableStateFlow<List<CropIssue>?>(null)

    init {
//        readOrders()
//        readHandcrafts

    }


    fun readOrders() {
        viewModelScope.launch {
            cropIssueRepository.getCropIssues().catch {
                failureMessage.value = it.message
            }
                .collect {
                    dataa.value = it
                }
        }
    }

    fun readAllOrders() {
        viewModelScope.launch {
            cropIssueRepository.getCropIssues().catch {
                failureMessage.value = it.message
                Log.i("ABC", "Failed ")
            }
                .collect {
                    dataa.value = it
                    Log.i("ABC", it.firstOrNull()!!.title!!)
                    Log.i("ABC", "RESTut form viw model ")
                }
        }
    }
//fun readHandcrafts() {
//    viewModelScope.launch {
//        orderRepository.getOrders().catch {
//            failureMessage.value = it.message
//        }
//            .collect {
//                data.value = it
//            }
//    }
//}
}