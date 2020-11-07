package com.ahmedkhaled.photoweatherapp.ui.history.adapters;

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.ahmedkhaled.photoweatherapp.R
import com.ahmedkhaled.photoweatherapp.models.Image
import com.ahmedkhaled.photoweatherapp.ui.history.FullScreenActivity
import com.squareup.picasso.Picasso
import java.io.File


class HistoryImagesAdapter(private val context: Context, private val items: ArrayList<Image>) :
    RecyclerView.Adapter<HistoryImagesAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        Picasso.get().load(items[position].file).into(holder.image)

        holder.image.setOnClickListener {
            val newIntent = Intent(context, FullScreenActivity::class.java)
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val files: ArrayList<File> = ArrayList()
            items.forEach {
                files.add(it.file)
            }
            newIntent.putExtra("Files", files)
            newIntent.putExtra("Position", position)
            context.startActivity(newIntent)
        }

        holder.twitterBtn.setOnClickListener {
            val file = items[position].file
            val imageUri = FileProvider.getUriForFile(
                context,
                "com.ahmedkhaled.photoweatherapp.fileprovider",
                file
            )
            try {
                val newIntent = Intent(Intent.ACTION_SEND)
                newIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
                newIntent.type = "image/jpg"
                newIntent.setPackage("com.twitter.android")
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(newIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context, "You don't have Twitter app", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Error Happened, Try Again!", Toast.LENGTH_SHORT).show()
            }
        }

        holder.facebookBtn.setOnClickListener {
            val file = items[position].file
            val imageUri = FileProvider.getUriForFile(
                context,
                "com.ahmedkhaled.photoweatherapp.fileprovider",
                file
            )
            try {
                val newIntent = Intent(Intent.ACTION_SEND)
                newIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
                newIntent.type = "image/jpg"
                newIntent.setPackage("com.facebook.katana")
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(newIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context, "You don't have Twitter app", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Error Happened, Try Again!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.itemImage)
        val twitterBtn: ImageView = itemView.findViewById(R.id.twitterBtn)
        val facebookBtn: ImageView = itemView.findViewById(R.id.facebookBtn)
    }

}