package com.diman.sipenguji.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.diman.sipenguji.R
import com.diman.sipenguji.model.Gedung
import com.diman.sipenguji.network.ApiConfig
import kotlinx.android.synthetic.main.fragment_detail_gedung.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailGedungFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_gedung, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayData()
    }

    fun displayData(){
        val id = activity?.intent?.getStringExtra("id_gedung")!!.toInt()
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
                        .load(gambar)
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
}