package com.example.searchapi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.searchapi.data.Document
import com.example.searchapi.databinding.ItemListBinding
import java.text.SimpleDateFormat

class ImageAdapter(var imageList : MutableList<Document>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(document: Document) {
            with(binding) {
                Glide.with(itemView)
                    .load(document.thumbnailUrl)
                    .into(ivThumbnail)
                tvTitle.text = document.siteName
                tvTime.text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(document.datetime)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ImageAdapter.ImageViewHolder {
        return ImageViewHolder(
            ItemListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val document = imageList[position]
        holder.bind(document)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }
}