package com.ahmedkhaled.photoweatherapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ahmedkhaled.photoweatherapp.R
import com.ahmedkhaled.photoweatherapp.ui.camera.CameraFragment
import com.ahmedkhaled.photoweatherapp.ui.history.HistoryFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {

    companion object {
        private const val CAMERA = 0
        private const val HISTORY = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        loadUI()
    }

    private fun loadUI() {
        pager.offscreenPageLimit = 3
        val adapter = ViewPagerAdapter(this)
        adapter.addFragment(R.drawable.ic_baseline_camera, CameraFragment(this))
        adapter.addFragment(R.drawable.ic_baseline_history, HistoryFragment(this))
        pager.adapter = adapter

        TabLayoutMediator(
            pagerTL, pager
        ) { tab, position ->
            tab.icon = ContextCompat.getDrawable(this, adapter.getFragmentIcon(position))
        }
            .attach()
        pagerTL.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    CAMERA -> {
                        tab.icon?.setTint(ContextCompat.getColor(this@HomeActivity, R.color.green))
                    }
                    HISTORY -> {
                        tab.icon?.setTint(ContextCompat.getColor(this@HomeActivity, R.color.purple))
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                tab.icon?.setTint(ContextCompat.getColor(this@HomeActivity, R.color.black))
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        pagerTL.getTabAt(CAMERA)?.icon?.setTint(ContextCompat.getColor(this@HomeActivity, R.color.green))
        pagerTL.getTabAt(HISTORY)?.icon?.setTint(ContextCompat.getColor(this@HomeActivity, R.color.black))

    }
}