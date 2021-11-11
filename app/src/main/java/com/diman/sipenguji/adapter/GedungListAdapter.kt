package com.diman.sipenguji.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.diman.sipenguji.DetailGedungActivity
import com.diman.sipenguji.MainActivity
import com.diman.sipenguji.R
import com.diman.sipenguji.fragment.EditGedungFragment
import com.diman.sipenguji.fragment.HomeGedungFragment
import com.diman.sipenguji.model.DataItem
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.item_gedung.view.*
import kotlinx.android.synthetic.main.item_gedung_list.view.*

class GedungListAdapter (val gedung : MutableList<DataItem>, val fragmentManager: FragmentManager) : RecyclerView.Adapter<GedungListAdapter.GedungListHolder>() {

    //fungsi untuk menampilkan recyclerView saat dijalankan
    //fungsi ini mengambil referensi layout item yang akan ditampilkan
    // dan mengembalikannya ke kelas GedungHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GedungListHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_gedung_list, parent, false)
        return GedungListHolder(view)
    }

    //fungsi holder untuk melakukan binding value yang akan di isi dari
    //komponen yang telah di assign dari class GedungHolder
    override fun onBindViewHolder(holder: GedungListHolder, position: Int) {
        val  _gedung = gedung[position]

        Log.d("img", _gedung.gambar.toString())

//        Glide.with(holder.itemView.context)
//            .load("https://images.unsplash.com/photo-1523050854058-8df90110c9f1?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80")
//            .apply(RequestOptions().centerCrop().placeholder(R.drawable.banner_img))
//            .into(holder.ivGambarGedung)

        holder.tvNamaGedung.text = _gedung.namaGedung
        holder.tvAlamatGedung.text = _gedung.alamat

        holder.rvGedung.setOnClickListener {
            val id = _gedung.id
            val activity = holder.itemView.context
            showDetailGedung(activity, id)
        }

        holder.btnEdit.setOnClickListener {
            //button edit clicked
            val a = EditGedungFragment(_gedung.id!!.toInt())
            a.show(fragmentManager, "")
        }

        holder.btnDelete.setOnClickListener {
            //button delete clicked
        }
    }

    //fungsi untuk hitung jumlah data
    // dan mengembalikan jumlahnya
    override fun getItemCount(): Int {
        return gedung.size
    }

    //class yang melakukan assign(mengisi) komponen yang akan ditampilkan
    //kedalan recyclerView
    class GedungListHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
        var ivGambarGedung = itemView.iv_gedung_img
        var tvNamaGedung = itemView.tv_namagedung_home_list
        var tvAlamatGedung = itemView.tv_alamatGedung_home_list
        var btnEdit = itemView.btn_home_gedung_edit
        var btnDelete = itemView.btn_home_gedung_delete
        var rvGedung = itemView.cv_item_gedung_list
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