package com.example.ecoscan.ui.home

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
import com.example.ecoscan.data.model.article
import com.example.ecoscan.databinding.ItemArticleBinding
import com.example.ecoscan.ui.web.WebViewActivity

class ArticelAdapter: ListAdapter<article, ArticelAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticelAdapter.MyViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticelAdapter.MyViewHolder, position: Int) {
        val scansItem = getItem(position)
        holder.bind(scansItem)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, WebViewActivity::class.java)
            intent.putExtra("url", scansItem.url)
            val ivScanImage = holder.itemView.findViewById<ImageView>(R.id.image)
            val tvDate = holder.itemView.findViewById<TextView>(R.id.title)
            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    holder.itemView.context as Activity,
                    Pair(ivScanImage, "image"),
                    Pair(tvDate, "title"),
                )
            holder.itemView.context.startActivity(intent, optionsCompat.toBundle())
        }
    }

    class MyViewHolder(private val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: article) {
            binding.title.text = item.title
            Glide.with(binding.image)
                .load(item.photo) // URL gambar
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.image)
        }
    }

    companion object {

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<article>() {
            override fun areItemsTheSame(oldItem: article, newItem: article): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(
                oldItem: article,
                newItem: article
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}