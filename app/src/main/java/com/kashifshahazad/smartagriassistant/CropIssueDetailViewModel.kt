package com.kashifshahazad.smartagriassistant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CropIssueDetailViewModel:ViewModel() {
    val isSaving = MutableStateFlow<Boolean?>(false)
    val isSaved = MutableStateFlow<Boolean?>(null)
    val isFailure = MutableStateFlow<String?>(null)
    val cropRepository = CropIssueRepository()

    val dataComments = MutableStateFlow<List<Comment>?>(null)
    val failureMessage = MutableStateFlow<String?>(null)

    val commentRepository = CommentRepository()


    fun readComments(postId:String) {
        viewModelScope.launch {
            commentRepository.getCommentsOfCurrentPost(postId).catch {
                failureMessage.value = it.message
            }
                .collect {
                    dataComments.value = it
                }
        }
    }
    fun addComment(comment: Comment) {
        isSaving.value = true
        viewModelScope.launch {
            val result = commentRepository.saveComment(comment)
            if (result.isSuccess) {
                isSaving.value = false
                isSaved.value = true
            } else {
                isFailure.value = result.exceptionOrNull()?.message
            }
        }
    }

    fun update(cropIssue: CropIssue) {
        viewModelScope.launch {
            isSaving.value=true
            val result = cropRepository.updateRequest(cropIssue)
            if (result.isSuccess) {
                isSaved.value = true
                isSaving.value= false
            } else {
                isSaving.value= false
                isFailure.value = result.exceptionOrNull()?.message
            }
        }
    }

}