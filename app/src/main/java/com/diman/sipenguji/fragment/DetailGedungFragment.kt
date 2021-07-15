package com.diman.sipenguji.fragment

import android.content.Intent
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
import com.diman.sipenguji.adapter.RuanganAdapter
import com.diman.sipenguji.adapter.RuanganListAdapter
import com.diman.sipenguji.model.DataRuangan
import com.diman.sipenguji.model.Gedung
import com.diman.sipenguji.model.Ruangan
import com.diman.sipenguji.network.ApiConfig
import kotlinx.android.synthetic.main.fragment_detail_gedung.*
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class DetailGedungFragment : Fragment() {

    private lateinit var ruanganListAdapter: RuanganListAdapter
    private var idGedung : Int? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_gedung, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ruanganListAdapter = RuanganListAdapter(mutableListOf())

        rv_ruangan_list.setHasFixedSize(true)
        rv_ruangan_list.layoutManager = LinearLayoutManager(activity)
        rv_ruangan_list.adapter = ruanganListAdapter

        displayDataGedung()
        displayDataRuangan()
    }

    fun displayDataGedung(){
        val id = activity?.intent?.getStringExtra("id_gedung")!!.toInt()
        idGedung = id
        val client = ApiConfig.getApiService().getGedung(id)
        client.enqueue(object : Callback<Gedung>{
            override fun onResponse(call: Call<Gedung>, response: Response<Gedung>) {
                if(response.isSuccessful){
                    Log.d("Response", "Connected To API")
                    val data = response.body()?.data?.get(0)

                    val namaGedung = data?.namaGedung
                    val gambar = data?.gambar
                    val jumlahRuangan = data?.jumlahRuangan
                    val latitude = data?.latitude
                    val longitude = data?.longitude

                    Glide.with(requireActivity())
                        .load("https://images.unsplash.com/photo-1494145904049-0dca59b4bbad?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=334&q=80")
                        .apply(RequestOptions().centerCrop().placeholder(R.drawable.banner_img))
                        .into(iv_image_gedung)

                    tv_nama_gedung.text = namaGedung
                    tv_jumlah_ruangan.text = jumlahRuangan
                    tv_latitude.text = latitude
                    tv_longitude.text = longitude
                }
            }

            override fun onFailure(call: Call<Gedung>, t: Throwable) {
                Log.d("Response", "Failed to connect API with message ${t.printStackTrace()}")
            }

        })
    }

    fun displayDataRuangan(){
        val client = ApiConfig.getApiService().getListRuanganByGedungId(idGedung!!)

        client.enqueue(object : Callback<Ruangan>{
            override fun onResponse(call: Call<Ruangan>, response: Response<Ruangan>) {
                if (response.isSuccessful){
                    Log.d("Response", "Connection to API Endpoint Ruangan is successful")
                    val dataRuangan = response.body()?.data as List<DataRuangan>
                    for (data in dataRuangan){
                        ruanganListAdapter.addDataRuanganList(data)
                    }
                } else {
                    Log.d("Response", "Failed connect to API Endpoint Ruangan")
                }
            }

            override fun onFailure(call: Call<Ruangan>, t: Throwable) {
                Log.e("Response", "Failed connect to API with message ${t.printStackTrace()}")
            }

        })
    }

}