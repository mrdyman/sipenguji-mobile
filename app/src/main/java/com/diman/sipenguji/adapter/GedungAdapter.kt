package com.diman.sipenguji.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diman.sipenguji.R
import com.diman.sipenguji.model.DataItem
import kotlinx.android.synthetic.main.card_gedung.view.*
import kotlinx.android.synthetic.main.item_gedung.view.*

class GedungAdapter (val gedung : MutableList<DataItem>) : RecyclerView.Adapter<GedungAdapter.GedungHolder>() {

    //fungsi untuk menampilkan recyclerView saat dijalankan
    //fungsi ini mengambil referensi layout item yang akan ditampilkan
    // dan mengembalikannya ke kelas GedungHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GedungHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.card_gedung, parent, false)
        return GedungHolder(view)
    }

    //fungsi holder untuk melakukan binding value yang akan di isi dari
    //komponen yang telah di assign dari class GedungHolder
    override fun onBindViewHolder(holder: GedungHolder, position: Int) {
        val  _gedung = gedung[position]

        holder.tvNamaGedung.text = _gedung.namaGedung
        holder.tvAlamatGedung.text = _gedung.alamat
    }

    //fungsi untuk hitung jumlah data
    // dan mengembalikan jumlahnya
    override fun getItemCount(): Int {
        return gedung.size
    }

    //class yang melakukan assign(mengisi) komponen yang akan ditampilkan
    //kedalan recyclerView
    class GedungHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
        var tvNamaGedung = itemView.tv_gedungName
        var tvAlamatGedung = itemView.tv_alamatGedung

    }

    //fungsi untuk masukkan data gedung kedalam recycleview
    fun addGedung(newDataGedung: DataItem){
        gedung.add(newDataGedung)
        notifyItemInserted(gedung.lastIndex)
    }
}