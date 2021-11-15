package com.diman.sipenguji.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.diman.sipenguji.DetailRuanganActivity
import com.diman.sipenguji.R
import com.diman.sipenguji.fragment.DetailGedungFragment
import com.diman.sipenguji.fragment.EditGedungFragment
import com.diman.sipenguji.fragment.EditRuanganFragment
import com.diman.sipenguji.model.DataPeserta
import com.diman.sipenguji.model.DataRuangan
import com.diman.sipenguji.model.Ruangan
import com.diman.sipenguji.network.ApiConfig
import kotlinx.android.synthetic.main.dialog_delete_ruangan.*
import kotlinx.android.synthetic.main.dialog_delete_ruangan.btn_close_dialog_deleteRuangan
import kotlinx.android.synthetic.main.dialog_failed.*
import kotlinx.android.synthetic.main.dialog_ruangan_deleted.*
import kotlinx.android.synthetic.main.dialog_upload_image.*
import kotlinx.android.synthetic.main.dialog_upload_image.btn_close_dialog_uploadImage
import kotlinx.android.synthetic.main.item_peserta.view.*
import kotlinx.android.synthetic.main.item_ruangan.view.*
import kotlinx.android.synthetic.main.item_ruangan_list.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PesertaUjianAdapter (val pesertaList : MutableList<DataPeserta>, val fragmentManager: FragmentManager) : RecyclerView.Adapter<PesertaUjianAdapter.PesertaListHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PesertaListHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_peserta, parent, false)
        return PesertaListHolder(view)
    }

    override fun onBindViewHolder(holder: PesertaListHolder, position: Int) {
        val _pesertaList = pesertaList[position]

        Glide.with(holder.itemView.context)
            .load("https://images.unsplash.com/photo-1523050854058-8df90110c9f1?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80")
            .apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_user))
            .into(holder.fotoPeserta)

        holder.namaPeserta.text = _pesertaList.nama
        holder.nisn.text = _pesertaList.nisn
        holder.nik.text = _pesertaList.nik
        holder.nomorPeserta.text = _pesertaList.nomorPeserta
    }

    override fun getItemCount(): Int {
        return pesertaList.size
    }

    class PesertaListHolder (itemView : View): RecyclerView.ViewHolder(itemView) {
        val fotoPeserta = itemView.iv_foto_peserta
        val namaPeserta = itemView.tv_namaPeserta
        val nisn = itemView.tv_nisn_peserta
        val nik = itemView.tv_nik_peserta
        val nomorPeserta = itemView.tv_nomor_peserta
    }

    fun addDataPesertaList(newDataPeserta : DataPeserta){
        pesertaList.add(newDataPeserta)
        notifyItemInserted(pesertaList.lastIndex)
    }
}