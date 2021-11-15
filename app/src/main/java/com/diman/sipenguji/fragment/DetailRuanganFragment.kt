package com.diman.sipenguji.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.diman.sipenguji.R
import com.diman.sipenguji.adapter.PesertaUjianAdapter
import com.diman.sipenguji.adapter.RuanganListAdapter
import com.diman.sipenguji.model.*
import com.diman.sipenguji.network.ApiConfig
import kotlinx.android.synthetic.main.fragment_detail_gedung.*
import kotlinx.android.synthetic.main.fragment_detail_ruangan.*
import kotlinx.android.synthetic.main.fragment_detail_ruangan.iv_image_gedung
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailRuanganFragment() : Fragment() {

    private lateinit var pesertaUjianAdapter: PesertaUjianAdapter
    private var idRuangan : Int? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_ruangan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pesertaUjianAdapter = PesertaUjianAdapter(mutableListOf(), requireActivity().supportFragmentManager)

        rv_peserta.setHasFixedSize(true)
        rv_peserta.layoutManager = LinearLayoutManager(activity)
        rv_peserta.adapter = pesertaUjianAdapter

        displayDataRuangan()
        displayDataPeserta()

        btn_detail_ruangan_back.setOnClickListener {
            activity?.finish()
        }
    }

    private fun displayDataRuangan() {
        val id = activity?.intent?.getStringExtra("id_ruangan")!!.toInt()
        idRuangan = id
        val client = ApiConfig.getApiService().getRuangan(idRuangan!!)
        client.enqueue(object : Callback<Ruangan>{
            override fun onResponse(call: Call<Ruangan>, response: Response<Ruangan>) {
                if(response.isSuccessful){
                    Log.d("Response", "Connected To API Detail Gedung")
                    val data = response.body()?.data!![0]

                    val namaRuangan = data?.namaRuangan
                    val kelompokUjian = data?.jenisUjian
                    val jumlahPeserta = data?.jumlahPeserta
                    val alamat = data?.namaGedung

                    Glide.with(activity!!.baseContext)
                        .load("https://images.unsplash.com/photo-1494145904049-0dca59b4bbad?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=334&q=80")
                        .apply(RequestOptions().centerCrop().placeholder(R.drawable.banner_img))
                        .into(iv_image_gedung)

                    tv_nama_ruangan_detail.text = namaRuangan
                    tv_kelompok_ujian_detail.text = kelompokUjian
                    tv_jumlah_peserta_detail.text = jumlahPeserta
                    tv_alamatRuangan_detail.text = alamat
                } else {
                    Log.d("Response", "Connecting to API Detail Gedung Unsuccessful with message ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Ruangan>, t: Throwable) {
                Log.d("Response", "Failed to connect API Detail Gedung with message ${t.message}")
            }

        })
    }

    fun displayDataPeserta(){
        val client = ApiConfig.getApiService().getPeserta(idRuangan!!)

        client.enqueue(object : Callback<Peserta>{
            override fun onResponse(call: Call<Peserta>, response: Response<Peserta>) {
                if (response.isSuccessful){
                    Log.d("Response", "Connection to API Endpoint Peserta Ujian is successful")
                    val dataPeserta = response.body()?.data as List<DataPeserta>
                    Log.d("Response", dataPeserta.toString())
                    for (data in dataPeserta){
                        pesertaUjianAdapter.addDataPesertaList(data)
                    }
                } else {
                    showEmptyData()
                    Log.d("Response", "Failed connect to API Endpoint Peserta Ujian")
                }
            }

            override fun onFailure(call: Call<Peserta>, t: Throwable) {
                Log.e("Response", "Failed connect to API Peserta Ujian with message ${t.printStackTrace()}")
            }

        })
    }

    private fun showEmptyData()
    {
        rv_peserta.visibility = View.GONE
        iv_pesertaUjian_not_found.visibility = View.VISIBLE
    }
}