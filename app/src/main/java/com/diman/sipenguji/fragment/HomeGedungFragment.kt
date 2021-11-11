package com.diman.sipenguji.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.diman.sipenguji.MainActivity
import com.diman.sipenguji.R
import com.diman.sipenguji.adapter.GedungAdapter
import com.diman.sipenguji.adapter.GedungListAdapter
import com.diman.sipenguji.model.DataItem
import com.diman.sipenguji.model.Gedung
import com.diman.sipenguji.network.ApiConfig
import kotlinx.android.synthetic.main.fragment_home_gedung.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeGedungFragment : Fragment() {

    private lateinit var gedungListAdapter: GedungListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_gedung, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gedungListAdapter = GedungListAdapter(mutableListOf(), requireActivity().supportFragmentManager)

        rv_gedung_home_list.setHasFixedSize(true)
        rv_gedung_home_list.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_gedung_home_list.adapter = gedungListAdapter

        getGedung()
        viewListener()
    }

    private fun viewListener() {

        cv_home_gedung_show_gedung.setCardBackgroundColor(resources.getColor(R.color.colorPrimary, activity?.theme))
        tv_home_gedung_menu_gedung.setTextColor(resources.getColor(R.color.white, activity?.theme))

        cv_home_gedung_show_all.setOnClickListener {
            //show main menu fragment
            cv_home_gedung_show_all.setCardBackgroundColor(resources.getColor(R.color.colorPrimary, activity?.theme))
            tv_home_gedung_mainMenu.setTextColor(resources.getColor(R.color.white, activity?.theme))

            cv_home_gedung_show_gedung.setCardBackgroundColor(resources.getColor(R.color.white, activity?.theme))
            tv_home_gedung_menu_gedung.setTextColor(resources.getColor(R.color.colorPrimary, activity?.theme))

            cv_home_gedung_show_ruangan.setCardBackgroundColor(resources.getColor(R.color.white, activity?.theme))
            tv_home_gedung_menu_ruangan.setTextColor(resources.getColor(R.color.colorPrimary, activity?.theme))

            val activity = (activity as MainActivity)
            activity.replaceFragment(HomeFragment1())
        }

        cv_home_gedung_show_gedung.setOnClickListener {
            cv_home_gedung_show_gedung.setCardBackgroundColor(resources.getColor(R.color.colorPrimary, activity?.theme))
            tv_home_gedung_menu_gedung.setTextColor(resources.getColor(R.color.white, activity?.theme))

            cv_home_gedung_show_all.setCardBackgroundColor(resources.getColor(R.color.white, activity?.theme))
            tv_home_gedung_mainMenu.setTextColor(resources.getColor(R.color.colorPrimary, activity?.theme))

            cv_home_gedung_show_ruangan.setCardBackgroundColor(resources.getColor(R.color.white, activity?.theme))
            tv_home_gedung_menu_ruangan.setTextColor(resources.getColor(R.color.colorPrimary, activity?.theme))

            val activity = (activity as MainActivity)
            activity.replaceFragment(HomeGedungFragment())
        }

        cv_home_gedung_show_ruangan.setOnClickListener {
            cv_home_gedung_show_ruangan.setCardBackgroundColor(resources.getColor(R.color.colorPrimary, activity?.theme))
            tv_home_gedung_menu_ruangan.setTextColor(resources.getColor(R.color.white, activity?.theme))

            cv_home_gedung_show_all.setCardBackgroundColor(resources.getColor(R.color.white, activity?.theme))
            tv_home_gedung_mainMenu.setTextColor(resources.getColor(R.color.colorPrimary, activity?.theme))

            cv_home_gedung_show_gedung.setCardBackgroundColor(resources.getColor(R.color.white, activity?.theme))
            tv_home_gedung_menu_gedung.setTextColor(resources.getColor(R.color.colorPrimary, activity?.theme))

            val activity = (activity as MainActivity)
            activity.replaceFragment(HomeRuanganFragment())
        }
    }

    private fun getGedung() {
        val client = ApiConfig.getApiService().getListGedung()

        client.enqueue(object : Callback<Gedung> {
            override fun onResponse(call: Call<Gedung>, response: Response<Gedung>) {
                if (response.isSuccessful) {
                    val dataArray = response.body()?.data as List<DataItem>
                    for (data in dataArray) {
                        gedungListAdapter.addGedung(data)
                    }
                }
            }

            override fun onFailure(call: Call<Gedung>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }
}