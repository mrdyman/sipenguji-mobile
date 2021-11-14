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
import com.diman.sipenguji.model.DataRuangan
import com.diman.sipenguji.model.Ruangan
import com.diman.sipenguji.network.ApiConfig
import kotlinx.android.synthetic.main.dialog_delete_ruangan.*
import kotlinx.android.synthetic.main.dialog_delete_ruangan.btn_close_dialog_deleteRuangan
import kotlinx.android.synthetic.main.dialog_failed.*
import kotlinx.android.synthetic.main.dialog_ruangan_deleted.*
import kotlinx.android.synthetic.main.dialog_upload_image.*
import kotlinx.android.synthetic.main.dialog_upload_image.btn_close_dialog_uploadImage
import kotlinx.android.synthetic.main.item_ruangan.view.*
import kotlinx.android.synthetic.main.item_ruangan_list.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RuanganListAdapter (val ruanganList : MutableList<DataRuangan>, val fragmentManager: FragmentManager) : RecyclerView.Adapter<RuanganListAdapter.RuanganListHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RuanganListHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_ruangan_list, parent, false)
        return RuanganListHolder(view)
    }

    override fun onBindViewHolder(holder: RuanganListHolder, position: Int) {
        val _ruanganList = ruanganList[position]

        holder.namaRuangan.text = _ruanganList.namaRuangan
        holder.jenisUjian.text = _ruanganList.jenisUjian
        holder.jumlahPeserta.text = _ruanganList.jumlahPeserta
        holder.alamatRuangan.text = _ruanganList.namaGedung

        holder.btnEdit.setOnClickListener {
            val editRuangan = EditRuanganFragment(_ruanganList.id!!.toInt())
            editRuangan.show(fragmentManager, "")
        }

        holder.btnDelete.setOnClickListener {
            val id = _ruanganList.id
            val context = holder.itemView.context
            showDialogDelete(id!!.toInt(), context, position)
        }

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
        val namaRuangan = itemView.tv_namaruangan_home_list
        val jenisUjian = itemView.tv_kelompok_ujian
        val jumlahPeserta = itemView.tv_jumlah_peserta_home_list
        val alamatRuangan = itemView.tv_alamat_ruangan_list
        val rvRuangan = itemView.cv_item_ruangan_list
        val btnEdit = itemView.btn_home_ruangan_edit
        val btnDelete = itemView.btn_home_ruangan_delete
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

    private fun showDialogDelete(id: Int, context: Context, position: Int){
        val builder = AlertDialog.Builder(context)
        builder.setView(R.layout.dialog_delete_ruangan)

        val dialog: AlertDialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.show()

        dialog.btn_dialog_deleteRuangan.setOnClickListener {
            val client = ApiConfig.getApiService().deleteRuangan(id)
            Log.d("Response", id.toString())
            client.enqueue(object : Callback<Ruangan>{
                override fun onResponse(call: Call<Ruangan>, response: Response<Ruangan>) {
                    if (response.isSuccessful){
                        Log.d("Response", "Connection to API delete Ruangan successful with message ${response.body()?.status}")

                        ruanganList.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(0, ruanganList.size)

                        showSuccessDialog(context)
                        dialog.dismiss()
                    } else {
                        Log.d("Response", "Connection to API delete Ruangan Unsuccessful with message ${response.message()}")
                        showFailedDialog(context)
                        dialog.dismiss()
                    }
                }

                override fun onFailure(call: Call<Ruangan>, t: Throwable) {
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

        dialog.btn_close_dialog_deleteRuangan.setOnClickListener {
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