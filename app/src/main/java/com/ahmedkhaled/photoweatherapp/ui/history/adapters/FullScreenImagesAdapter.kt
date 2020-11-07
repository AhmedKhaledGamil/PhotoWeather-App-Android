package com.ahmedkhaled.photoweatherapp.ui.history.adapters;

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.ahmedkhaled.photoweatherapp.R
import com.ahmedkhaled.photoweatherapp.models.Image
import com.squareup.picasso.Picasso

class FullScreenImagesAdapter(private val context: Context, private val items: ArrayList<Image>) :
    RecyclerView.Adapter<FullScreenImagesAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_image_full_screen, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        Picasso.get().load(items[position].file).into(holder.image)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.itemImageFullScreen)
    }

}