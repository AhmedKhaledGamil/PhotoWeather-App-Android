package com.ahmedkhaled.photoweatherapp.ui.history

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.ahmedkhaled.photoweatherapp.R
import com.ahmedkhaled.photoweatherapp.models.Image
import com.ahmedkhaled.photoweatherapp.ui.history.adapters.FullScreenImagesAdapter
import kotlinx.android.synthetic.main.activity_full_screen.*
import java.io.File


class FullScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen)

        @Suppress("UNCHECKED_CAST")
        val files = intent.getSerializableExtra("Files") as ArrayList<File>
        val images: ArrayList<Image> = ArrayList()
        files.forEach {
            images.add(Image(it))
        }
        fullScreenRV.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        fullScreenRV.adapter = FullScreenImagesAdapter(this, images)
        val selectedPosition = intent.getIntExtra("Position", 0)
        val snapHelper: LinearSnapHelper = object : LinearSnapHelper() {
            override fun findTargetSnapPosition(
                layoutManager: RecyclerView.LayoutManager,
                velocityX: Int,
                velocityY: Int
            ): Int {
                val centerView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
                val position = layoutManager.getPosition(centerView)
                var targetPosition = -1
                if (layoutManager.canScrollHorizontally()) {
                    targetPosition = if (velocityX < 0) {
                        position - 1
                    } else {
                        position + 1
                    }
                }
                if (layoutManager.canScrollVertically()) {
                    targetPosition = if (velocityY < 0) {
                        position - 1
                    } else {
                        position + 1
                    }
                }
                val firstItem = 0
                val lastItem = layoutManager.itemCount - 1
                targetPosition = lastItem.coerceAtMost(targetPosition.coerceAtLeast(firstItem))
                return targetPosition
            }
        }
        snapHelper.attachToRecyclerView(fullScreenRV)
        fullScreenRV.scrollToPosition(selectedPosition)
    }
}