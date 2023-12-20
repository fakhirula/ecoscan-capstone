package com.example.ecoscan.ui.resultimage

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityOptionsCompat
import androidx.core.net.toUri
import com.example.ecoscan.R
import com.example.ecoscan.databinding.ActivityImageBinding
import com.example.ecoscan.databinding.ItemGetStartedBinding
import com.example.ecoscan.databinding.ItemLoginRegisterBinding
import com.example.ecoscan.databinding.ItemResultBinding
import com.example.ecoscan.ui.ViewModelFactory
import com.example.ecoscan.ui.home.HomeActivity
import com.example.ecoscan.ui.home.reduceFileImage
import com.example.ecoscan.ui.home.uriToFile
import com.example.ecoscan.ui.login.LoginActivity
import com.example.ecoscan.ui.main.MainActivity
import com.example.ecoscan.ui.profile.ProfileActivity
import com.example.ecoscan.ui.signup.SignupActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class ImageActivity : AppCompatActivity() {
    private val viewModel by viewModels<ResultViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityImageBinding
    private var currentImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAction()
    }

    private fun setupAction() {
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
        viewModel.alert.observe(this) {
            showAlert(it.success, it.message)
        }
        currentImageUri = intent.getStringExtra("IMAGE_URI")?.toUri()
        binding.imageResult.apply {
            setImageURI(currentImageUri)
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
        binding.btnUpload.setOnClickListener {
            binding.btnUpload.visibility =View.GONE
            uploadImage()
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val photo = MultipartBody.Part.createFormData(
                "attachment",
                imageFile.name,
                requestImageFile
            )
            viewModel.getSession().observe(this) { user ->
                viewModel.uploadImage(photo, user.token)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.loadingText.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showAlert(success: Boolean, message: String) {
        if (!success) {
            AlertDialog.Builder(this).apply {
                setTitle(R.string.alertFailure)
                if (message=="Oh no! Your token seems to be invalid. Please check and try again."){
                    setMessage(message)
                    setPositiveButton(R.string.login) { dialog, _ ->
                        viewModel.logout()
                        val intent = Intent(context, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                }else{
                    setMessage(message)
                    setPositiveButton(R.string.ButtonAlert) { dialog, _ ->
                        val intent = Intent(context, HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                }
            }.show()
        }else{
            showResult()
        }
    }

    private fun showResult() {
        val sheetDialog = BottomSheetDialog(this)
        val binding = ItemResultBinding.inflate(layoutInflater)
        sheetDialog.apply {
            setContentView(binding.root)
            show()
            binding.btnProfie.setOnClickListener {
                val intent = Intent(context, ProfileActivity::class.java )
                startActivity(intent)
                finish()
            }
            viewModel.waste.observe(this) {
                binding.waste.text = getString(R.string.the_waste_is, it)
            }
        }
    }
}