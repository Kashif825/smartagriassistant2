package com.kashifshahazad.smartagriassistant

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.kashifshahazad.smartagriassistant.databinding.ActivityAddCropIssueBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import java.text.SimpleDateFormat

class add_crop_issue : AppCompatActivity() {
//    private var uri: Uri? = null
    lateinit var binding:  ActivityAddCropIssueBinding;
    lateinit var viewModel: AddCropIssueViewModel
    lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var currentUid="";
        authViewModel=AuthViewModel()
        authViewModel.checkUser()
        binding = ActivityAddCropIssueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = AddCropIssueViewModel()

        lifecycleScope.launch {
            viewModel.isSuccessfullySaved.collect {
                it?.let {
                    if (it == true) {
                        Toast.makeText(
                            this@add_crop_issue,
                            "Successfully saved",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        finish()
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewModel.failureMessage.collect {
                it?.let {
                    Toast.makeText(this@add_crop_issue, it, Toast.LENGTH_SHORT).show()
                }
            }
        }
        lifecycleScope.launch {
            authViewModel.currentUser.collect{
                it?.let {
                    currentUid = it.uid
                }
            }
        }

        binding.submitButton.setOnClickListener {
            val title = binding.titleInput.text.toString().trim()
            val description = binding.descriptionInput.text.toString().trim()

            // Validate the input fields
            if (title.isEmpty() || description.isEmpty() ) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            // Create a Handcraft object with the entered data
            val cropIssue = CropIssue()
            cropIssue.date= SimpleDateFormat("yyyy-MM-dd HH:mm a").format(System.currentTimeMillis())
            cropIssue.title = title
            cropIssue.description = description
            cropIssue.userId = currentUid


//            if (uri == null)
            viewModel.saveCropIssue(cropIssue)
//            else
//                viewModel.uploadImageAndSaveHandCraft(getRealPathFromURI(uri!!)!!, handcraft)

            // Save the Handcraft object (this would be a database operation, Firestore, etc.)
            // For now, just display the success message
            Toast.makeText(this, "Your Issue is Listed Successfully!", Toast.LENGTH_SHORT).show()

        }

        binding.imageView2.setOnClickListener {
            chooseImageFromGallery()
        }

    }

    private fun chooseImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        galleryLauncher.launch(intent)
    }

//    private val galleryLauncher = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ) { result: Instrumentation.ActivityResult ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            uri = result.data?.data
//            if (uri != null) {
//                binding.imageView2.setImageURI(uri)
//            } else {
//                Log.e("Gallery", "No image selected")
//            }
//        }
//    }

    private fun getRealPathFromURI(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            if (cursor.moveToFirst()) {
                return cursor.getString(columnIndex)
            }
        }
        return null
    }
}