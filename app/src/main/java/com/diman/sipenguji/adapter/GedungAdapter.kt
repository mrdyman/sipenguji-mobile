package com.diman.sipenguji.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.diman.sipenguji.DetailGedungActivity
import com.diman.sipenguji.MainActivity
import com.diman.sipenguji.R
import com.diman.sipenguji.model.DataItem
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.item_gedung.view.*

class GedungAdapter (val gedung : MutableList<DataItem>) : RecyclerView.Adapter<GedungAdapter.GedungHolder>() {

    //fungsi untuk menampilkan recyclerView saat dijalankan
    //fungsi ini mengambil referensi layout item yang akan ditampilkan
    // dan mengembalikannya ke kelas GedungHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GedungHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_gedung, parent, false)
        return GedungHolder(view)
    }

    //fungsi holder untuk melakukan binding value yang akan di isi dari
    //komponen yang telah di assign dari class GedungHolder
    override fun onBindViewHolder(holder: GedungHolder, position: Int) {
        val  _gedung = gedung[position]

        Log.d("img", _gedung.gambar.toString())

        Glide.with(holder.itemView.context)
            .load("https://pbs.twimg.com/media/E5whIYxVUAABYZm?format=png&name=240x240")
            .apply(RequestOptions().centerCrop().placeholder(R.drawable.banner_img))
            .into(holder.ivGambarGedung)

        holder.tvNamaGedung.text = _gedung.namaGedung
        holder.tvAlamatGedung.text = _gedung.alamat

        holder.rvGedung.setOnClickListener {
            val id = _gedung.id
            val activity = holder.itemView.context
            showDetailGedung(activity, id)
        }
    }

    //fungsi untuk hitung jumlah data
    // dan mengembalikan jumlahnya
    override fun getItemCount(): Int {
        return gedung.size
    }

    //class yang melakukan assign(mengisi) komponen yang akan ditampilkan
    //kedalan recyclerView
    class GedungHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
        var ivGambarGedung = itemView.iv_gedung_img
        var tvNamaGedung = itemView.tv_gedungName
        var tvAlamatGedung = itemView.tv_alamatGedung
        var rvGedung = itemView.cv_gedung
    }

    //fungsi untuk masukkan data gedung kedalam recycleview
    fun addGedung(newDataGedung: DataItem){
        gedung.add(newDataGedung)
        notifyItemInserted(gedung.lastIndex)
    }

    fun showDetailGedung(context: Context, id: String?){
        var i = Intent(context, DetailGedungActivity::class.java)
        i.putExtra("id_gedung", id)
        context.startActivity(i)
    }
}