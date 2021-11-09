package com.diman.sipenguji.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.diman.sipenguji.R
import com.diman.sipenguji.adapter.HistoryLayoutAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vp_history.adapter = HistoryLayoutAdapter(requireActivity())

        val tabl = TabLayoutMediator(tl_history, vp_history) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Gedung"
                    tab.icon = ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_campus,
                        null
                    )
                }
                1 -> {
                    tab.text = "Ruangan"
                    tab.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_building, null)
                }
            }
        }
        tabl.attach()
    }
}