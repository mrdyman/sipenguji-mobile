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
import kotlinx.android.synthetic.main.list_gambar_gedung.view.*

class GambarAdapter (val gambar : MutableList<DataGambar>) : RecyclerView.Adapter<GambarAdapter.GambarHolder>() {

    //fungsi untuk menampilkan recyclerView saat dijalankan
    //fungsi ini mengambil referensi layout item yang akan ditampilkan
    // dan mengembalikannya ke kelas GambarHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GambarHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_gambar_gedung, parent, false)
        return GambarHolder(view)
    }

    //fungsi holder untuk melakukan binding value yang akan di isi dari
    //komponen yang telah di assign dari class GambarHolder
    override fun onBindViewHolder(holder: GambarHolder, position: Int) {
        val  _gambar = gambar[position]

//        Log.d("diman-array", _gambar.nama!!)

        Log.d("img", _gambar.nama.toString())

        Glide.with(holder.itemView.context)
            .load("https://images.unsplash.com/photo-1523050854058-8df90110c9f1?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80")
            .apply(RequestOptions().centerCrop().placeholder(R.drawable.banner_img))
            .into(holder.ivGambarRuangan)

        holder.rvGambarRuangan.setOnClickListener {
//            val id = _gambar.id
            val activity = holder.itemView.context
            val listGambar = _gambar.nama
//            Log.d("diman-array", gambar)
//            showDetailGedung(activity, id)
            showGallery(activity, listGambar!!)
        }
    }

    //fungsi untuk hitung jumlah data
    // dan mengembalikan jumlahnya
    override fun getItemCount(): Int {
        return gambar.size
    }

    //class yang melakukan assign(mengisi) komponen yang akan ditampilkan
    //kedalan recyclerView
    class GambarHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
        var ivGambarRuangan = itemView.iv_gambar_gedung
        var rvGambarRuangan = itemView.cv_img_ruangan
    }

    //fungsi untuk masukkan data gedung kedalam recycleview
    fun addGambar(newDataGambar: DataGambar){
        gambar.add(newDataGambar)
        notifyItemInserted(gambar.lastIndex)
        Log.d("Response", "${newDataGambar.nama}")
    }

    fun showDetailGedung(context: Context, id: String?){
        var i = Intent(context, DetailGedungActivity::class.java)
        i.putExtra("id_gedung", id)
        context.startActivity(i)
    }

    private fun showGallery(context: Context, listGambar : String){
        val i = Intent(context, GalleryActivity::class.java)
        i.putExtra("gambar", listGambar)
        context.startActivity(i)
    }
}