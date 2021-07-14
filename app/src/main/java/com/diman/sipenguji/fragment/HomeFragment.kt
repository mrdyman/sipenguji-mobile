package com.diman.sipenguji.fragment

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afdhal_fa.imageslider.`interface`.ItemClickListener
import com.afdhal_fa.imageslider.model.SlideUIModel
import com.diman.sipenguji.R
import com.diman.sipenguji.adapter.GedungAdapter
import com.diman.sipenguji.model.Banner
import com.diman.sipenguji.model.DataBanner
import com.diman.sipenguji.model.DataItem
import com.diman.sipenguji.model.Gedung
import com.diman.sipenguji.network.ApiConfig
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var adapter: GedungAdapter

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = GedungAdapter(mutableListOf())

        rv_gedung.setHasFixedSize(true)
        rv_gedung.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rv_gedung.adapter = adapter

        getGedung()
        loadBanner()
    }

    private fun getGedung() {
        val client = ApiConfig.getApiService().getListGedung()

        client.enqueue(object : Callback<Gedung> {
            override fun onResponse(call: Call<Gedung>, response: Response<Gedung>) {
                if (response.isSuccessful) {
                    val dataArray = response.body()?.data as List<DataItem>
                    for (data in dataArray) {
                        adapter.addGedung(data)
                    }
                }
            }

            override fun onFailure(call: Call<Gedung>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    fun loadBanner(){
        val bannerList = ArrayList<SlideUIModel>()

        //request banner ke API
        val client = ApiConfig.getApiService().getListBanner()
        client.enqueue(object : Callback<Banner>{
            override fun onResponse(call: Call<Banner>, response: Response<Banner>) {
                Log.d("Response", "Successful connected to API Endpoint Banner")
                if (response.isSuccessful){
                    val dataBanner = response.body()?.data as List<DataBanner>
                    for (data in dataBanner){
                        bannerList.add(SlideUIModel(data.gambar.toString(), data.link))
                    }
                    is_banner.setImageList(bannerList)
                } else {
                    Log.d("Response", "Failed to connect to API EndPoint")
                }
            }

            override fun onFailure(call: Call<Banner>, t: Throwable) {
                Log.d("Response", "Failed to connect to API with message ${t.printStackTrace()}")
            }

        })

        //add listener to handle onClick banner
        is_banner.setItemClickListener(object : ItemClickListener {
            override fun onItemClick(model: SlideUIModel, position: Int) {
                Toast.makeText(requireActivity(), "${model.title}", Toast.LENGTH_SHORT).show()
            }
        })

    }
}