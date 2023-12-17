package com.example.ecoscan.ui.home

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecoscan.data.model.ArticleDummy
import com.example.ecoscan.data.model.article
import com.example.ecoscan.databinding.ActivityHomeBinding
import com.example.ecoscan.databinding.DialogCameraBinding
import com.example.ecoscan.remote.response.ListScansItem
import com.example.ecoscan.ui.profile.ProfileActivity
import com.example.ecoscan.ui.profile.ScanAdapter
import com.example.ecoscan.ui.resultimage.ImageActivity
import com.google.android.material.bottomsheet.BottomSheetDialog

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private var currentImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
    }

    private fun setupView() {
        binding.rvArticle.layoutManager = LinearLayoutManager(this)
    }

    private fun setupAction() {
        videoPlayer()
        binding.btnProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        binding.btnCamera.setOnClickListener { setupCamera() }
        val articleList: List<article> = ArticleDummy.Article
        setScan(articleList)
    }

    private fun setupCamera() {
        val sheetDialog = BottomSheetDialog(this)
        val binding = DialogCameraBinding.inflate(layoutInflater)
        sheetDialog.apply {
            setContentView(binding.root)
            show()
            binding.btnCamera.setOnClickListener {
                startCamera()
                dismiss()
            }
            binding.btnGaleri.setOnClickListener {
                startGalery()
                dismiss()
            }
        }
    }

    private fun startGalery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            val intent = Intent(this, ImageActivity::class.java)
            intent.putExtra("IMAGE_URI", it.toString())
            startActivity(intent)
        }
    }

    private fun videoPlayer() {
        val videoItem = MediaItem.fromUri("https://storage.googleapis.com/ecoscan-submission/video_tentang_sampah_1.mp4")
        val player = ExoPlayer.Builder(this).build().also { exoPlayer ->
            exoPlayer.setMediaItem(videoItem)
            exoPlayer.prepare()
            exoPlayer.shuffleModeEnabled
        }
        binding.playerView.player = player
    }

    private fun setScan(listScan: List<article>?) {
        val adapter= ArticelAdapter()
        adapter.submitList(listScan)
        binding.rvArticle.adapter = adapter
    }
}