package com.example.ecoscan.ui.detail

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.ecoscan.R
import com.example.ecoscan.databinding.ActivityDetailScanBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailScanActivity : AppCompatActivity() {
    private lateinit var binding:ActivityDetailScanBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDetailScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val id = intent.getStringExtra("id")
        val wasteType = intent.getStringExtra("waste")
        val date = formatDate(intent.getStringExtra("date").toString())
        val url = intent.getStringExtra("url")

        Log.d("DetailScanActivity","$id , $wasteType, $date, $url")

        if (url != null){
            binding.tvWaste.text = getString(R.string.type_waste, wasteType)
            binding.tvDate.text = getString(R.string.date, date)
            Glide.with(binding.ivScanImage)
                .load(url) // URL gambar
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.ivScanImage)
        }
    }


    fun formatDate(inputDate: String): String {
        try {
            // Parse tanggal dari string ISO 8601
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val date: Date = inputFormat.parse(inputDate) ?: return ""

            // Format ulang tanggal ke format yang diinginkan
            val outputFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm:ss", Locale.getDefault())
            return outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            return ""
        }
    }
}