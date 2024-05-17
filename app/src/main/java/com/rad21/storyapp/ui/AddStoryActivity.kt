package com.rad21.storyapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.rad21.storyapp.MainActivity
import com.rad21.storyapp.databinding.ActivityAddStoryBinding
import com.rad21.storyapp.getImageUri
import com.rad21.storyapp.reduceFileImage
import com.rad21.storyapp.uriToFile
import com.rad21.storyapp.viewmodel.AddStoryViewModel
import com.rad21.storyapp.viewmodel.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var addStoryViewModel: AddStoryViewModel
    private var currentImageUri: Uri? = null

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addStoryViewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(this)
        )[AddStoryViewModel::class.java]

        addStoryViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        binding.btnCamera.setOnClickListener { startCamera() }

        binding.btnGallery.setOnClickListener { startGallery() }

        binding.buttonAdd.setOnClickListener {
            currentImageUri?.let { uri ->
                val imageFile = uriToFile(uri, this)
                val reduceFile = reduceFileImage(imageFile)
                val description = binding.edAddDescription.text.toString()

                val descriptionRequestBody = description.toRequestBody("text/plain".toMediaType())
                val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())

                val imageMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    reduceFile.name,
                    requestImageFile

                )

                addStoryViewModel.getSession().observe(this) { user ->
                    if (user.isLogin) {
                        val token = user.token
                        addStoryViewModel.addStory(token, imageMultiPart, descriptionRequestBody)
                        addStoryViewModel.uploadResponse.observe(this) { response ->
                            if (response.error == false) {
                                val builder = AlertDialog.Builder(this@AddStoryActivity)
                                builder.setTitle("Successfull!")
                                builder.setMessage("Story Uploaded")
                                builder.setPositiveButton("Next") { _, _ ->
                                    val intent =
                                        Intent(this@AddStoryActivity, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()

                                }
                                val dialog = builder.create()
                                dialog.show()

                            }
                        }
                    }
                }
            }
        }

        addStoryViewModel.isUpload.observe(this) { response ->
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Upload Failed")
            builder.setMessage(response)
            builder.setPositiveButton("Back") { _, _ ->
            }
            val dialog = builder.create()
            dialog.show()
        }

    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        currentImageUri?.let {
            launchCamera.launch(it)
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private val launchCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSucces ->
        if (isSucces) {
            showImage()
        }
    }


    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivPhotoStory.setImageURI(it)
        }
    }

}