package com.diman.sipenguji.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.diman.sipenguji.R
import com.diman.sipenguji.model.DataRuangan
import kotlinx.android.synthetic.main.item_ruangan.view.*

class RuanganAdapter (val ruangan : MutableList<DataRuangan>) : RecyclerView.Adapter<RuanganAdapter.RuanganHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RuanganHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_ruangan, parent, false)
        return RuanganHolder(view)
    }

    override fun onBindViewHolder(holder: RuanganHolder, position: Int) {
        val _ruangan = ruangan[position]

        holder.namaRuangan.text = _ruangan.namaRuangan

        Glide.with(holder.itemView.context)
            .load(_ruangan.gambar)
            .apply(RequestOptions().centerCrop().placeholder(R.drawable.banner_img))
            .into(holder.gambarRuangan)
    }

    override fun getItemCount(): Int {
        return ruangan.size
    }

    class RuanganHolder (itemView : View): RecyclerView.ViewHolder(itemView) {
        val namaRuangan = itemView.tv_nama_ruangan
        val gambarRuangan = itemView.iv_gambar_ruangan
    }

    fun addDataRuangan(newDataRuangan : DataRuangan){
        ruangan.add(newDataRuangan)
        notifyItemInserted(ruangan.lastIndex)
    }

}