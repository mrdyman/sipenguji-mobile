package com.diman.sipenguji.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.diman.sipenguji.DetailGedungActivity
import com.diman.sipenguji.GalleryActivity
import com.diman.sipenguji.MainActivity
import com.diman.sipenguji.R
import com.diman.sipenguji.model.DataGambar
import com.diman.sipenguji.model.DataItem
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.item_gedung.view.*
import kotlinx.android.synthetic.main.item_gedung_list.view.*
import kotlinx.android.synthetic.main.list_gambar_gedung.view.*

class HistoryGedungAdapter (val historyGedung : MutableList<DataItem>) : RecyclerView.Adapter<HistoryGedungAdapter.HistoryGedungHolder>() {

    //fungsi untuk menampilkan recyclerView saat dijalankan
    //fungsi ini mengambil referensi layout item yang akan ditampilkan
    // dan mengembalikannya ke kelas HistoryGedungHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryGedungHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_gedung_list, parent, false)
        return HistoryGedungHolder(view)
    }

    //fungsi holder untuk melakukan binding value yang akan di isi dari
    //komponen yang telah di assign dari class HistoryGedungHolder
    override fun onBindViewHolder(holder: HistoryGedungHolder, position: Int) {
        val  _historyGedung = historyGedung[position]

        holder.namaGedung.text = _historyGedung.namaGedung
        holder.alamatGedung.text = _historyGedung.alamat

        holder.rvHistoryGedung.setOnClickListener {
            val activity = holder.itemView.context
            val id = _historyGedung.id
            showDetail(activity, id)
        }
    }

    //fungsi untuk hitung jumlah data
    // dan mengembalikan jumlahnya
    override fun getItemCount(): Int {
        return historyGedung.size
    }

    //class yang melakukan assign(mengisi) komponen yang akan ditampilkan
    //kedalan recyclerView
    class HistoryGedungHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
        var rvHistoryGedung = itemView.cv_item_gedung_list
        var namaGedung = itemView.tv_namagedung_home_list
        var alamatGedung = itemView.tv_alamatGedung_home_list
        var btnEdit = itemView.btn_home_gedung_edit
        var btnHapus = itemView.btn_home_gedung_delete
    }

    //fungsi untuk masukkan data HistoryGedung kedalam recycleview
    fun addData(newDataHistory: DataItem){
        historyGedung.add(newDataHistory)
        notifyItemInserted(historyGedung.lastIndex)
        Log.d("Response", "${newDataHistory.namaGedung}")
    }

    fun showDetail(context: Context, id: String?){
        var i = Intent(context, DetailGedungActivity::class.java)
        i.putExtra("id_gedung", id)
        context.startActivity(i)
    }
}