package com.kashifshahazad.smartagriassistant

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class CropIssueRepository {
    val cropIssueCollection = FirebaseFirestore.getInstance().collection("cropissue")

    suspend fun saveCropIssue(cropIssue: CropIssue): Result<Boolean> {
        try {
            val document = cropIssueCollection.document()
            cropIssue.postId = document.id
            document.set(cropIssue).await()
            return Result.success(true)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
    suspend fun updateRequest(cropIssue: CropIssue): Result<Boolean> {
        try {
            if (cropIssue.postId.isNullOrEmpty()) {
                return Result.failure(IllegalArgumentException("Order ID (rid) is null or empty"))
            }

            cropIssueCollection.document(cropIssue.postId!!).set(cropIssue).await()
            return Result.success(true)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }


    fun getCropIssues() =
        cropIssueCollection.snapshots().map { it.toObjects(CropIssue::class.java) }

    fun getCropIssueOfUser(phoneNumber: String) =
        cropIssueCollection.whereEqualTo("phoneNumber",phoneNumber).snapshots().map { it.toObjects(CropIssue::class.java)
        }



}

