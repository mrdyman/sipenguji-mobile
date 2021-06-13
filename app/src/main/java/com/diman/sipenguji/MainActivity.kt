package com.diman.sipenguji

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.diman.sipenguji.adapter.GedungAdapter
import com.diman.sipenguji.model.DataItem
import com.diman.sipenguji.model.Gedung
import com.diman.sipenguji.network.ApiConfig
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: GedungAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = GedungAdapter(mutableListOf())

        rv_gedung.setHasFixedSize(true)
        rv_gedung.layoutManager = LinearLayoutManager(this)
        rv_gedung.adapter = adapter

        getGedung()
    }

    fun getGedung(){
        val client = ApiConfig.getApiService().getListGedung()

        client.enqueue(object : Callback<Gedung>{
            override fun onResponse(call: Call<Gedung>, response: Response<Gedung>) {
                if (response.isSuccessful){
                    val dataArray = response.body()?.data as List<DataItem>
                    for (data in dataArray){
                        adapter.addGedung(data)
                    }
                }
            }

            override fun onFailure(call: Call<Gedung>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }
}