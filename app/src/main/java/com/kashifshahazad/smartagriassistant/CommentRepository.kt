package com.kashifshahazad.smartagriassistant

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class CommentRepository {
    val commentCollection = FirebaseFirestore.getInstance().collection("comments")

    suspend fun saveComment(comment: Comment): Result<Boolean> {
        try {
            val document = commentCollection.document()
            comment.commentId = document.id
            document.set(comment).await()
            return Result.success(true)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
    suspend fun updateComment(comment: Comment): Result<Boolean> {
        try {
            if (comment.commentId.isNullOrEmpty()) {
                return Result.failure(IllegalArgumentException("Order ID (rid) is null or empty"))
            }

            commentCollection.document(comment.commentId!!).set(comment).await()
            return Result.success(true)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }


    fun getComments() =
        commentCollection.snapshots().map { it.toObjects(Comment::class.java) }

    fun getCommentsOfCurrentPost(postId: String) =
        commentCollection.whereEqualTo("postId",postId).snapshots().map { it.toObjects(Comment::class.java)
        }



}

