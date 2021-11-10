package com.diman.sipenguji.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.diman.sipenguji.fragment.HistoryGedungFragment
import com.diman.sipenguji.fragment.HistoryRuanganFragment

class HistoryLayoutAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HistoryGedungFragment()
            else -> HistoryRuanganFragment()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}