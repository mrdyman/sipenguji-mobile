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
import kotlinx.android.synthetic.main.item_ruangan_list.view.*

class RuanganListAdapter (val ruanganList : MutableList<DataRuangan>) : RecyclerView.Adapter<RuanganListAdapter.RuanganListHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RuanganListHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_ruangan_list, parent, false)
        return RuanganListHolder(view)
    }

    override fun onBindViewHolder(holder: RuanganListHolder, position: Int) {
        val _ruanganList = ruanganList[position]

//        Glide.with(holder.itemView.context)
//            .load("https://images.unsplash.com/photo-1494145904049-0dca59b4bbad?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=334&q=80")
//            .apply(RequestOptions().centerCrop().placeholder(R.drawable.banner_img))
//            .into(holder.gambarRuangan)

        holder.namaRuangan.text = _ruanganList.namaRuangan
        holder.jadwalHari.text = _ruanganList.jadwal
        holder.jumlahPeserta.text = _ruanganList.jumlahPeserta

        holder.rvRuangan.setOnClickListener {
            val activity = holder.itemView.context
            val id = _ruanganList.id
            showDetailRuangan(activity, id)
        }
    }

    override fun getItemCount(): Int {
        return ruanganList.size
    }

    class RuanganListHolder (itemView : View): RecyclerView.ViewHolder(itemView) {
//        val gambarRuangan = itemView.iv_foto_ruangan
        val namaRuangan = itemView.tv_namaruangan_home_list
        val jadwalHari = itemView.tv_hari_tgl_home_list
        val jadwalJam = itemView.tv_jam_home_list
        val jumlahPeserta = itemView.tv_jumlah_peserta_home_list
        val rvRuangan = itemView.cv_item_ruangan_list
    }

    fun addDataRuanganList(newDataRuangan : DataRuangan){
        ruanganList.add(newDataRuangan)
        notifyItemInserted(ruanganList.lastIndex)
    }

    fun showDetailRuangan(context: Context, id: String?){
        val i = Intent(context, DetailRuanganActivity::class.java)
        i.putExtra("id_ruangan", id)
        context.startActivity(i)
    }
}