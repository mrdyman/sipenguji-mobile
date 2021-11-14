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
import com.diman.sipenguji.*
import com.diman.sipenguji.model.DataGambar
import com.diman.sipenguji.model.DataItem
import com.diman.sipenguji.model.DataRuangan
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.item_gedung.view.*
import kotlinx.android.synthetic.main.item_gedung_list.view.*
import kotlinx.android.synthetic.main.item_gedung_list.view.btn_home_gedung_delete
import kotlinx.android.synthetic.main.item_gedung_list.view.btn_home_gedung_edit
import kotlinx.android.synthetic.main.item_ruangan_list.view.*
import kotlinx.android.synthetic.main.list_gambar_gedung.view.*

class HistoryRuanganAdapter (val historyRuangan : MutableList<DataRuangan>) : RecyclerView.Adapter<HistoryRuanganAdapter.HistoryRuanganHolder>() {

    //fungsi untuk menampilkan recyclerView saat dijalankan
    //fungsi ini mengambil referensi layout item yang akan ditampilkan
    // dan mengembalikannya ke kelas HistoryGedungHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryRuanganHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_ruangan_list, parent, false)
        return HistoryRuanganHolder(view)
    }

    //fungsi holder untuk melakukan binding value yang akan di isi dari
    //komponen yang telah di assign dari class HistoryGedungHolder
    override fun onBindViewHolder(holder: HistoryRuanganHolder, position: Int) {
        val  _historyRuangan = historyRuangan[position]

        holder.namaRuangan.text = _historyRuangan.namaRuangan
        holder.jenisUjian.text = _historyRuangan.jenisUjian
        holder.jumlahPeserta.text = _historyRuangan.jumlahPeserta
        holder.alamatRuangan.text = _historyRuangan.namaGedung
        holder.rvHistoryRuangan.setOnClickListener {
            val activity = holder.itemView.context
            val id = _historyRuangan.id
            showDetail(activity, id)
        }
    }

    //fungsi untuk hitung jumlah data
    // dan mengembalikan jumlahnya
    override fun getItemCount(): Int {
        return historyRuangan.size
    }

    //class yang melakukan assign(mengisi) komponen yang akan ditampilkan
    //kedalan recyclerView
    class HistoryRuanganHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
        var rvHistoryRuangan = itemView.cv_item_ruangan_list
        var namaRuangan = itemView.tv_namaruangan_home_list
        val jenisUjian = itemView.tv_kelompok_ujian
        var jumlahPeserta = itemView.tv_jumlah_peserta_home_list
        val alamatRuangan = itemView.tv_alamat_ruangan_list
        var btnEdit = itemView.btn_home_ruangan_edit
        var btnHapus = itemView.btn_home_ruangan_delete
    }

    //fungsi untuk masukkan data HistoryGedung kedalam recycleview
    fun addData(newDataHistory: DataRuangan){
        historyRuangan.add(newDataHistory)
        notifyItemInserted(historyRuangan.lastIndex)
        Log.d("Response", "${newDataHistory.namaGedung}")
    }

    fun showDetail(context: Context, id: String?){
        var i = Intent(context, DetailRuanganActivity::class.java)
        i.putExtra("id_ruangan", id)
        context.startActivity(i)
    }
}