package com.diman.sipenguji.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.diman.sipenguji.model.DataRuangan
import com.diman.sipenguji.model.Gedung
import com.diman.sipenguji.model.Ruangan
import com.diman.sipenguji.network.ApiConfig
import kotlinx.android.synthetic.main.dialog_delete_ruangan.*
import kotlinx.android.synthetic.main.dialog_failed.*
import kotlinx.android.synthetic.main.dialog_ruangan_deleted.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.item_gedung.view.*
import kotlinx.android.synthetic.main.item_gedung_list.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
            val editGedung = EditGedungFragment(_gedung.id!!.toInt())
            editGedung.show(fragmentManager, "")
        }

        holder.btnDelete.setOnClickListener {
            //button delete clicked
            val id = _gedung.id
            val context = holder.itemView.context
            showDialogDelete(id!!.toInt(), context, position)
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

    private fun showDialogDelete(id: Int, context: Context, position: Int){
        val builder = AlertDialog.Builder(context)
        builder.setView(R.layout.dialog_delete_ruangan)

        val dialog: AlertDialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.show()

        dialog.btn_dialog_deleteRuangan.setOnClickListener {
            val client = ApiConfig.getApiService().deleteGedung(id)
            Log.d("Response", id.toString())
            client.enqueue(object : Callback<Gedung> {
                override fun onResponse(call: Call<Gedung>, response: Response<Gedung>) {
                    if (response.isSuccessful){
                        Log.d("Response", "Connection to API delete Ruangan successful with message ${response.body()?.status}")

                        gedung.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(0, gedung.size)

                        showSuccessDialog(context)
                        dialog.dismiss()
                    } else {
                        Log.d("Response", "Connection to API delete Ruangan Unsuccessful with message ${response.message()}")
                        showFailedDialog(context)
                        dialog.dismiss()
                    }
                }

                override fun onFailure(call: Call<Gedung>, t: Throwable) {
                    Log.d("Response", "Connection to API delete Ruangan Failed with message ${t.message}")
                }

            })
        }

        dialog.btn_dialog_cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.btn_close_dialog_deleteRuangan.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun showSuccessDialog(context: Context){
        val builder = AlertDialog.Builder(context)
        builder.setView(R.layout.dialog_ruangan_deleted)

        val dialog: AlertDialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.show()

        dialog.btn_close_dialog_deletedRuangan.setOnClickListener {
            dialog.dismiss()
        }

        dialog.btn_dialog_ok.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun showFailedDialog(context: Context){
        val builder = AlertDialog.Builder(context)
        builder.setView(R.layout.dialog_failed)

        val dialog: AlertDialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.show()

        dialog.btn_close_dialog_failed.setOnClickListener {
            dialog.dismiss()
        }

        dialog.btn_dialog_tutup.setOnClickListener {
            dialog.dismiss()
        }
    }
}