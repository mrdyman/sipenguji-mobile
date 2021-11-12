package com.diman.sipenguji.fragment

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.diman.sipenguji.R
import com.diman.sipenguji.model.*
import com.diman.sipenguji.network.ApiConfig
import com.diman.sipenguji.network.UploadRequestBody
import com.diman.sipenguji.util.getFileName
import com.diman.sipenguji.util.snackbar
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_delete_ruangan.*
import kotlinx.android.synthetic.main.dialog_failed.*
import kotlinx.android.synthetic.main.dialog_ruangan_deleted.*
import kotlinx.android.synthetic.main.fragment_add_data.*
import kotlinx.android.synthetic.main.fragment_edit_gedung.*
import kotlinx.android.synthetic.main.fragment_edit_ruangan.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class EditRuanganFragment (val idRuangan: Int) : BottomSheetDialogFragment() {

    private var idGedung : Int? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_ruangan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        displayData()

        btn_update_ruangan.setOnClickListener {
            updateDataRuangan()
        }
    }

    private fun displayData() {
        val client = ApiConfig.getApiService().getRuangan(idRuangan)
        client.enqueue(object : Callback<Ruangan> {
            override fun onResponse(call: Call<Ruangan>, response: Response<Ruangan>) {
                if (response.isSuccessful) {
                    val data = response.body()?.data!![0]
                    et_nama_ruangan_edit.text = Editable.Factory.getInstance().newEditable(data?.namaRuangan)
                    tv_alamatRuangan_edit.text = Editable.Factory.getInstance().newEditable(data?.namaGedung)
                    et_jumlah_peserta_edit.text = Editable.Factory.getInstance().newEditable(data?.jumlahPeserta)
                    et_latitude_edit.text = Editable.Factory.getInstance().newEditable(data?.latitude)
                    et_longitude_edit.text = Editable.Factory.getInstance().newEditable(data?.longitude)
                    showListAlamat()
                } else {
                    showFailedDialog(activity!!)
                }
            }

            override fun onFailure(call: Call<Ruangan>, t: Throwable) {
                showFailedDialog(activity!!)
            }

        })
    }

    private fun showListAlamat(){
        val dataAlamat : MutableList<String> = ArrayList()
        val dataId : MutableList<Int> = ArrayList()
        val client = ApiConfig.getApiService().getListGedung()
        client.enqueue(object : Callback<Gedung>{
            override fun onResponse(call: Call<Gedung>, response: Response<Gedung>) {
                if (response.isSuccessful){
                    val dataArray = response.body()?.data as List<DataItem>
                    for (data in dataArray) {
                        dataAlamat.add(data.namaGedung!!)
                        dataId.add(data.id!!.toInt())
                    }
                    val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_alamat_ruangan, dataAlamat)
                    val tvAlamat = tv_alamatRuangan_edit
                    tvAlamat.setAdapter(adapter)
                    tvAlamat.setOnItemClickListener { parent, view, position, id ->
                        val selectedId = dataId[position]
                        idGedung = selectedId
                        Log.d("GedungPosition", selectedId.toString())
                    }
                } else {
                    showFailedDialog(activity!!)
                }
            }

            override fun onFailure(call: Call<Gedung>, t: Throwable) {
                showFailedDialog(activity!!)
            }

        })
    }

    private fun updateDataRuangan() {
        val namaRuangan = et_nama_ruangan_edit.text.toString().trim()
        val jumlahPeserta = et_jumlah_peserta_edit.text.toString().trim()
        val latitude = et_latitude_edit.text.toString().trim()
        val longitude = et_longitude_edit.text.toString().trim()
        val alamat = idGedung

        val data = DataRuangan()
        data.id = idRuangan.toString()
        data.namaRuangan = namaRuangan
        data.jumlahPeserta = jumlahPeserta
        data.latitude = latitude
        data.longitude = longitude
        data.idGedung = alamat.toString()

        val client = ApiConfig.getApiService().updateRuangan(data)

        client.enqueue(object : Callback<Ruangan> {
            override fun onResponse(call: Call<Ruangan>, response: Response<Ruangan>) {
                if (response.isSuccessful){
                    showSuccessDialog(activity!!, "Data berhasil diupdate")
                } else {
                    showFailedDialog(activity!!)
                }
            }

            override fun onFailure(call: Call<Ruangan>, t: Throwable) {
                showFailedDialog(activity!!)
            }

        })
    }

    private fun showSuccessDialog(context: Context, message : String){
        val builder = AlertDialog.Builder(context)
        builder.setView(R.layout.dialog_ruangan_deleted)

        val dialog: AlertDialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.show()

        dialog.tv_message_dialog.text = message

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