package com.kashifshahazad.smartagriassistant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AddCropIssueViewModel: ViewModel(){
    val cropIssueRepository = CropIssueRepository()
//    val storageRepository = StorageRepository()

    val isSuccessfullySaved = MutableStateFlow<Boolean?>(null)
    val failureMessage = MutableStateFlow<String?>(null)


    fun saveCropIssue(addCropIssue: CropIssue) {
        viewModelScope.launch {
            val result = cropIssueRepository.saveCropIssue(addCropIssue)
            if (result.isSuccess) {
                isSuccessfullySaved.value = true
            } else {
                failureMessage.value = result.exceptionOrNull()?.message
            }
        }
    }

}
