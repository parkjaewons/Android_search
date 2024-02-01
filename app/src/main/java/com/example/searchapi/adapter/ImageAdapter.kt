package com.example.searchapi.adapter

import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.searchapi.R
import com.example.searchapi.data.Document
import com.example.searchapi.databinding.ItemListBinding
import java.text.SimpleDateFormat

class ImageAdapter(
    var imageList: MutableList<Document>,
    private val likeImage: MutableList<Document>,
) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    interface ItemClick {
        fun onClick(document: Document, position: Int)
    }

    var itemClick: ItemClick? = null

    inner class ImageViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(document: Document, position: Int) {
            with(binding) {
                Glide.with(itemView)
                    .load(document.thumbnailUrl)
                    .into(ivThumbnail)
                tvTitle.text = document.siteName
                tvTime.text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(document.datetime)

                itemView.setOnClickListener {
                    val item = imageList[position]
                    item.isLike = !item.isLike
                    Log.d("bb", "gg : ${item.isLike}")
                    if (item.isLike) {
                        item.isLike
                        likeImage.add(item)
                        ivLike.visibility = VISIBLE
                        Log.d("cc", "cc : $item")
                    } else {
                        !item.isLike
                        likeImage.remove(item)
                        ivLike.visibility = INVISIBLE
                        Log.d("dd", "dd : $item")
                    }
                    notifyItemInserted(position)
                    notifyItemRemoved(position)
                }
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
        holder.bind(document, position)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }
}