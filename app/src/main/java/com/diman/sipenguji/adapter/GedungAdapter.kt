package com.diman.sipenguji.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.diman.sipenguji.model.DataItem

class GedungAdapter (private val gedung : MutableList<DataItem>) : RecyclerView.Adapter<GedungAdapter.GedungHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GedungHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: GedungHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    class GedungHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {

    }
}