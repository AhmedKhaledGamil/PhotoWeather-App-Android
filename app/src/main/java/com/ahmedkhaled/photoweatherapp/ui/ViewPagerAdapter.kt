package com.ahmedkhaled.photoweatherapp.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.util.*

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(
        fragmentActivity
    ) {
    private val mFragmentList = ArrayList<Fragment>()
    private val mFragmentIconList = ArrayList<Int>()

    fun addFragment(icon: Int, fragment: Fragment) {
        mFragmentList.add(fragment)
        mFragmentIconList.add(icon)
    }

    fun getFragmentIcon(position: Int): Int {
        return mFragmentIconList[position]
    }

    override fun createFragment(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getItemCount(): Int {
        return mFragmentList.size
    }
}