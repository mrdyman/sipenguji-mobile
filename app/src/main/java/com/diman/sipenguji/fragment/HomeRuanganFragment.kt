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
import com.diman.sipenguji.adapter.GedungListAdapter
import com.diman.sipenguji.adapter.RuanganListAdapter
import com.diman.sipenguji.model.DataRuangan
import com.diman.sipenguji.model.Ruangan
import com.diman.sipenguji.network.ApiConfig
import kotlinx.android.synthetic.main.fragment_home_gedung.*
import kotlinx.android.synthetic.main.fragment_home_gedung.rv_gedung_home_list
import kotlinx.android.synthetic.main.fragment_home_ruangan.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeRuanganFragment : Fragment() {

    private lateinit var ruanganListAdapter: RuanganListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_ruangan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ruanganListAdapter = RuanganListAdapter(mutableListOf())

        rv_ruangan_home_list.setHasFixedSize(true)
        rv_ruangan_home_list.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_ruangan_home_list.adapter = ruanganListAdapter

        getRuangan()
        viewListener()
    }

    private fun viewListener() {

        cv_home_ruangan_show_ruangan.setCardBackgroundColor(resources.getColor(R.color.colorPrimary, activity?.theme))
        tv_home_ruangan_menu_ruangan.setTextColor(resources.getColor(R.color.white, activity?.theme))

        cv_home_ruangan_show_all.setOnClickListener {
            //show main menu fragment
            cv_home_ruangan_show_all.setCardBackgroundColor(resources.getColor(R.color.colorPrimary, activity?.theme))
            tv_home_ruangan_mainMenu.setTextColor(resources.getColor(R.color.white, activity?.theme))

            cv_home_ruangan_show_gedung.setCardBackgroundColor(resources.getColor(R.color.white, activity?.theme))
            tv_home_ruangan_menu_gedung.setTextColor(resources.getColor(R.color.colorPrimary, activity?.theme))

            cv_home_ruangan_show_ruangan.setCardBackgroundColor(resources.getColor(R.color.white, activity?.theme))
            tv_home_ruangan_menu_ruangan.setTextColor(resources.getColor(R.color.colorPrimary, activity?.theme))

            val activity = (activity as MainActivity)
            activity.replaceFragment(HomeFragment1())
        }

        cv_home_ruangan_show_gedung.setOnClickListener {
            cv_home_ruangan_show_gedung.setCardBackgroundColor(resources.getColor(R.color.colorPrimary, activity?.theme))
            tv_home_ruangan_menu_gedung.setTextColor(resources.getColor(R.color.white, activity?.theme))

            cv_home_ruangan_show_all.setCardBackgroundColor(resources.getColor(R.color.white, activity?.theme))
            tv_home_ruangan_mainMenu.setTextColor(resources.getColor(R.color.colorPrimary, activity?.theme))

            cv_home_ruangan_show_ruangan.setCardBackgroundColor(resources.getColor(R.color.white, activity?.theme))
            tv_home_ruangan_menu_ruangan.setTextColor(resources.getColor(R.color.colorPrimary, activity?.theme))

            val activity = (activity as MainActivity)
            activity.replaceFragment(HomeGedungFragment())
        }

        cv_home_ruangan_show_ruangan.setOnClickListener {
            cv_home_ruangan_show_ruangan.setCardBackgroundColor(resources.getColor(R.color.colorPrimary, activity?.theme))
            tv_home_ruangan_menu_ruangan.setTextColor(resources.getColor(R.color.white, activity?.theme))

            cv_home_ruangan_show_all.setCardBackgroundColor(resources.getColor(R.color.white, activity?.theme))
            tv_home_ruangan_mainMenu.setTextColor(resources.getColor(R.color.colorPrimary, activity?.theme))

            cv_home_ruangan_show_gedung.setCardBackgroundColor(resources.getColor(R.color.white, activity?.theme))
            tv_home_ruangan_menu_gedung.setTextColor(resources.getColor(R.color.colorPrimary, activity?.theme))

            val activity = (activity as MainActivity)
            activity.replaceFragment(HomeRuanganFragment())
        }
    }


    fun getRuangan(){
        val client = ApiConfig.getApiService().getListRuangan()

        client.enqueue(object : Callback<Ruangan> {
            override fun onResponse(call: Call<Ruangan>, response: Response<Ruangan>) {
                if (response.isSuccessful){
                    Log.d("Response", "Connecting to API Endpoint Ruangan Successful")
                    val dataRuangan = response.body()?.data as List<DataRuangan>
                    for (data in dataRuangan){
                        ruanganListAdapter.addDataRuanganList(data)
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
}