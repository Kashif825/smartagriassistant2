package com.kashifshahazad.smartagriassistant

import CommentsAdapter
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.kashifshahazad.smartagriassistant.databinding.ActivityDetailsOfCropIssueBinding
import kotlinx.coroutines.launch

class Details_of_cropIssue : AppCompatActivity() {
    lateinit var adapter: CommentsAdapter
    lateinit var binding: ActivityDetailsOfCropIssueBinding
    lateinit var cropIssue:CropIssue
    lateinit var viewModel:CropIssueDetailViewModel
    lateinit var progressDialog: ProgressDialog
    lateinit var  authViewModel: AuthViewModel
    val items=ArrayList<Comment>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        viewModel= CropIssueDetailViewModel()

        binding= ActivityDetailsOfCropIssueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cropIssue = Gson().fromJson(intent.getStringExtra("data"),CropIssue::class.java)

        binding.textView31.text = cropIssue.title?.toString()
        binding.textView32.text = cropIssue.description?.toString()
        binding.textView28.text = cropIssue.date?.toString()
        binding.textView7.text = cropIssue.noComments?.toString()


        progressDialog= ProgressDialog(this)
        progressDialog.setMessage("Updating the Order Status...Please Update")
        progressDialog.setCancelable(false)
        authViewModel= AuthViewModel()

        viewModel.readComments(cropIssue.postId!!)


        adapter= CommentsAdapter(items)
        binding.recyclerview.adapter=adapter
        binding.recyclerview.layoutManager= LinearLayoutManager(this@Details_of_cropIssue)



        lifecycleScope.launch {
            viewModel.isSaving.collect {
//                progressDialog.show()
                if (it==true) {
                    Toast.makeText(
                        this@Details_of_cropIssue,
                        "Status Updated Successfully.",
                        Toast.LENGTH_SHORT
                    ).show()
                    progressDialog.dismiss()

                    finish()

                }
            }
        }
        lifecycleScope.launch {
            viewModel.dataComments.collect {
                it?.let {
                    items.clear()
                    items.addAll(it)
                    adapter.notifyDataSetChanged()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.isFailure.collect {
                it?.let {
                    Toast.makeText(this@Details_of_cropIssue, it, Toast.LENGTH_LONG).show()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.failureMessage.collect {
                it?.let {
                    Toast.makeText(this@Details_of_cropIssue, it, Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.UpdateComment.setOnClickListener {
            val commentt=binding.name.editText?.text.toString()
            if(commentt.isNotEmpty()){
                val comment = Comment()
                comment.postId = cropIssue.postId.toString()
                comment.comment = commentt
                comment.userId = authViewModel.currentUser.value?.uid.toString()
                viewModel.addComment(comment)
                cropIssue.noComments=cropIssue.noComments?.plus(1)
                viewModel.update(cropIssue)
            }
        }
    }
}