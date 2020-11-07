package com.ahmedkhaled.photoweatherapp.ui.history

import android.app.Activity
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahmedkhaled.photoweatherapp.R
import com.ahmedkhaled.photoweatherapp.models.Image
import com.ahmedkhaled.photoweatherapp.ui.history.adapters.HistoryImagesAdapter
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment(private val activity: Activity) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onResume() {
        super.onResume()
        loadImages()
    }

    private fun loadImages() {
        historyImagesRV.visibility = View.GONE
        noImagesFound.visibility = View.GONE

        val storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val listFiles = storageDir?.listFiles()

        if (listFiles != null) {
            if (listFiles.isEmpty()) {
                noImagesFound.visibility = View.VISIBLE
            } else {
                val images: ArrayList<Image> = ArrayList()
                listFiles.forEach {
                    val fileSize: Int = (it.length() / 1024).toInt()
                    if (fileSize > 0) {
                        images.add(Image(it))
                    } else {
                        // Properly Delete Any File that is created due to OnBack
                        it.delete()
                        if (it.exists()) {
                            it.canonicalFile.delete()
                            if (it.exists()) {
                                activity.applicationContext.deleteFile(it.name)
                            }
                        }
                    }
                }

                val adapter = HistoryImagesAdapter(activity, images)
                historyImagesRV.layoutManager =
                    GridLayoutManager(activity, 2, RecyclerView.VERTICAL, false)
                historyImagesRV.adapter = adapter
                historyImagesRV.visibility = View.VISIBLE
            }
        }
    }
}