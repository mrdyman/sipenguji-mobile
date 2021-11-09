package com.diman.sipenguji.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.diman.sipenguji.R
import com.diman.sipenguji.adapter.GedungListAdapter
import com.diman.sipenguji.adapter.HistoryGedungAdapter
import com.diman.sipenguji.model.DataItem
import com.diman.sipenguji.model.Gedung
import com.diman.sipenguji.network.ApiConfig
import kotlinx.android.synthetic.main.fragment_add_data.*
import kotlinx.android.synthetic.main.fragment_history_gedung.*
import kotlinx.android.synthetic.main.fragment_home_gedung.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryGedungFragment : Fragment() {

    private lateinit var historyGedungAdapter: HistoryGedungAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history_gedung, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historyGedungAdapter = HistoryGedungAdapter(mutableListOf())
        rv_history_gedung_list.setHasFixedSize(true)
        rv_history_gedung_list.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_history_gedung_list.adapter = historyGedungAdapter

        getGedungHistory()
    }

    private fun getGedungHistory() {
        val client = ApiConfig.getApiService().getLastGedung(5)

        client.enqueue(object : Callback<Gedung> {
            override fun onResponse(call: Call<Gedung>, response: Response<Gedung>) {
                if (response.isSuccessful) {
                    val dataArray = response.body()?.data as List<DataItem>
                    for (data in dataArray) {
                        historyGedungAdapter.addData(data)
                    }
                }
            }

            override fun onFailure(call: Call<Gedung>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }
}