package com.diman.sipenguji.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.diman.sipenguji.DetailRuanganActivity
import com.diman.sipenguji.R
import com.diman.sipenguji.model.DataRuangan
import kotlinx.android.synthetic.main.item_ruangan.view.*
import kotlinx.android.synthetic.main.list_ruangan_search.view.*

class RuanganAdapter(val ruangan: MutableList<DataRuangan>) :
    RecyclerView.Adapter<RuanganAdapter.RuanganHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RuanganHolder {
//        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_ruangan, parent, false)
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_ruangan_search, parent, false)
        return RuanganHolder(view)
    }

    override fun onBindViewHolder(holder: RuanganHolder, position: Int) {
        val _ruangan = ruangan[position]

        holder.namaRuangan.text = _ruangan.namaRuangan
        holder.alamatRuangan.text = _ruangan.namaGedung

        Glide.with(holder.itemView.context)
            .load("https://images.unsplash.com/photo-1494145904049-0dca59b4bbad?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=334&q=80")
            .apply(RequestOptions().centerCrop().placeholder(R.drawable.banner_img))
            .into(holder.gambarRuangan)

        holder.rvRuangan.setOnClickListener {
            val id = _ruangan.id
            val activity = holder.itemView.context
            showDetailRuangan(activity, id)
        }
    }

    override fun getItemCount(): Int {
        return ruangan.size
    }

    class RuanganHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //        val namaRuangan = itemView.tv_nama_ruangan
//        val gambarRuangan = itemView.iv_gambar_ruangan
//        val rvRuangan = itemView.cv_ruangan
        val gambarRuangan = itemView.iv_ruangan_ujian
        val rvRuangan = itemView.cv_ruangan_ujian
        val namaRuangan = itemView.nama_ruangan_ujian
        val alamatRuangan = itemView.jadwal_ujian
    }

    fun addDataRuangan(newDataRuangan: DataRuangan) {
        ruangan.add(newDataRuangan)
        notifyItemInserted(ruangan.lastIndex)
    }

    fun clearDataRuangan() {
        if (ruangan.isNotEmpty()) {
            ruangan.clear()
            notifyDataSetChanged()
        }
    }

    fun showDetailRuangan(context: Context, id: String?) {
        val i = Intent(context, DetailRuanganActivity::class.java)
        i.putExtra("id_ruangan", id)
        context.startActivity(i)
    }

}