package com.example.ecoscan.ui.profile

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.ecoscan.R
import com.example.ecoscan.databinding.ItemResultBinding
import com.example.ecoscan.databinding.ItemScanBinding
import com.example.ecoscan.remote.response.ListScansItem
import com.example.ecoscan.ui.detail.DetailScanActivity
import com.example.ecoscan.ui.home.HomeActivity
import com.example.ecoscan.ui.resultimage.ImageActivity
import com.example.ecoscan.ui.web.WebViewActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ScanAdapter: ListAdapter<ListScansItem, ScanAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanAdapter.MyViewHolder {
        val binding = ItemScanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScanAdapter.MyViewHolder, position: Int) {
        val scansItem = getItem(position)
        holder.bind(scansItem)
        holder.itemView.setOnClickListener {
            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, DetailScanActivity::class.java)
                intent.putExtra("id", scansItem.id)
                intent.putExtra("waste", scansItem.wasteType)
                intent.putExtra("date", scansItem.date)
                intent.putExtra("url", scansItem.attachment)
                val ivScanImage = holder.itemView.findViewById<ImageView>(R.id.iv_scan_image)
                val tvclassification = holder.itemView.findViewById<TextView>(R.id.tv_classification)
                val tvDate = holder.itemView.findViewById<TextView>(R.id.tv_date)
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        holder.itemView.context as Activity,
                        Pair(ivScanImage, "image"),
                        Pair(tvclassification, "classification"),
                        Pair(tvDate, "date")
                    )
                holder.itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    class MyViewHolder(private val binding: ItemScanBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListScansItem) {
            val formattedDate = formatDate(item.date)
            binding.tvDate.text = formattedDate
            binding.tvClassification.text = item.wasteType
            Glide.with(binding.ivScanImage)
                .load(item.attachment) // URL gambar
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.ivScanImage)
        }
        // Fungsi untuk mengubah format tanggal
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

    companion object {

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListScansItem>() {
            override fun areItemsTheSame(oldItem: ListScansItem, newItem: ListScansItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(
                oldItem: ListScansItem,
                newItem: ListScansItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}