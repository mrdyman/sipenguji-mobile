package com.diman.sipenguji.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.diman.sipenguji.MainActivity
import com.diman.sipenguji.R
import com.diman.sipenguji.adapter.GedungAdapter
import com.diman.sipenguji.adapter.RuanganAdapter
import com.diman.sipenguji.adapter.RuanganAdminAdapter
import com.diman.sipenguji.model.DataItem
import com.diman.sipenguji.model.DataRuangan
import com.diman.sipenguji.model.Gedung
import com.diman.sipenguji.model.Ruangan
import com.diman.sipenguji.network.ApiConfig
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home1.*
import kotlinx.android.synthetic.main.fragment_home1.rv_gedung
import kotlinx.android.synthetic.main.item_gedung.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment1 : Fragment() {

    private lateinit var gedungAdapter: GedungAdapter
    private lateinit var ruanganAdminAdapter: RuanganAdminAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewListener()
        gedungAdapter = GedungAdapter(mutableListOf())
        ruanganAdminAdapter = RuanganAdminAdapter(mutableListOf())

        rv_gedung.setHasFixedSize(true)
        rv_gedung.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rv_gedung.adapter = gedungAdapter

        rv_ruangan.setHasFixedSize(true)
        rv_ruangan.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rv_ruangan.adapter = ruanganAdminAdapter

        getGedung()
        getRuangan()
    }

    private fun getGedung() {
        val client = ApiConfig.getApiService().getListGedung()

        client.enqueue(object : Callback<Gedung> {
            override fun onResponse(call: Call<Gedung>, response: Response<Gedung>) {
                if (response.isSuccessful) {
                    val dataArray = response.body()?.data as List<DataItem>
                    for (data in dataArray) {
                        gedungAdapter.addGedung(data)
                    }
                }
            }

            override fun onFailure(call: Call<Gedung>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    fun getRuangan(){
        val client = ApiConfig.getApiService().getListRuangan()

        client.enqueue(object : Callback<Ruangan> {
            override fun onResponse(call: Call<Ruangan>, response: Response<Ruangan>) {
                if (response.isSuccessful){
                    Log.d("Response", "Connecting to API Endpoint Ruangan Successful")
                    val dataRuangan = response.body()?.data as List<DataRuangan>
                    for (data in dataRuangan){
                        ruanganAdminAdapter.addDataRuangan(data)
                    }
                } else {
                    Log.d("Response", "Connecting to API Endpoint Ruangan Failed!")
                }
            }

            override fun onFailure(call: Call<Ruangan>, t: Throwable) {
                Log.e("Response", "Failed connecting to API with message ${t.printStackTrace()}")
            }

        })
    }

    private fun viewListener() {

        cv_show_all.setCardBackgroundColor(resources.getColor(R.color.colorPrimary, activity?.theme))
        tv_mainMenu.setTextColor(resources.getColor(R.color.white, activity?.theme))

        cv_show_all.setOnClickListener {
            //show main menu fragment
            cv_show_all.setCardBackgroundColor(resources.getColor(R.color.colorPrimary, activity?.theme))
            tv_mainMenu.setTextColor(resources.getColor(R.color.white, activity?.theme))

            cv_show_gedung.setCardBackgroundColor(resources.getColor(R.color.white, activity?.theme))
            tv_menu_gedung.setTextColor(resources.getColor(R.color.colorPrimary, activity?.theme))

            cv_show_ruangan.setCardBackgroundColor(resources.getColor(R.color.white, activity?.theme))
            tv_menu_ruangan.setTextColor(resources.getColor(R.color.colorPrimary, activity?.theme))
        }

        cv_show_gedung.setOnClickListener {
            cv_show_gedung.setCardBackgroundColor(resources.getColor(R.color.colorPrimary, activity?.theme))
            tv_menu_gedung.setTextColor(resources.getColor(R.color.white, activity?.theme))

            cv_show_all.setCardBackgroundColor(resources.getColor(R.color.white, activity?.theme))
            tv_mainMenu.setTextColor(resources.getColor(R.color.colorPrimary, activity?.theme))

            cv_show_ruangan.setCardBackgroundColor(resources.getColor(R.color.white, activity?.theme))
            tv_menu_ruangan.setTextColor(resources.getColor(R.color.colorPrimary, activity?.theme))

            val activity = (activity as MainActivity)
            activity.replaceFragment(HomeGedungFragment())
        }

        cv_show_ruangan.setOnClickListener {
            cv_show_ruangan.setCardBackgroundColor(resources.getColor(R.color.colorPrimary, activity?.theme))
            tv_menu_ruangan.setTextColor(resources.getColor(R.color.white, activity?.theme))

            cv_show_all.setCardBackgroundColor(resources.getColor(R.color.white, activity?.theme))
            tv_mainMenu.setTextColor(resources.getColor(R.color.colorPrimary, activity?.theme))

            cv_show_gedung.setCardBackgroundColor(resources.getColor(R.color.white, activity?.theme))
            tv_menu_gedung.setTextColor(resources.getColor(R.color.colorPrimary, activity?.theme))

            val activity = (activity as MainActivity)
            activity.replaceFragment(HomeRuanganFragment())
        }
    }
}