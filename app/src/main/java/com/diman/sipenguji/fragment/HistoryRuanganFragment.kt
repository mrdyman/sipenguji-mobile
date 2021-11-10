package com.diman.sipenguji.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.diman.sipenguji.R
import com.diman.sipenguji.adapter.HistoryGedungAdapter
import com.diman.sipenguji.adapter.HistoryRuanganAdapter
import com.diman.sipenguji.model.DataItem
import com.diman.sipenguji.model.DataRuangan
import com.diman.sipenguji.model.Gedung
import com.diman.sipenguji.model.Ruangan
import com.diman.sipenguji.network.ApiConfig
import kotlinx.android.synthetic.main.fragment_history_gedung.*
import kotlinx.android.synthetic.main.fragment_history_ruangan.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryRuanganFragment : Fragment() {

    private lateinit var historyRuanganAdapter: HistoryRuanganAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history_ruangan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historyRuanganAdapter = HistoryRuanganAdapter(mutableListOf())

        rv_history_ruangan_list.setHasFixedSize(true)
        rv_history_ruangan_list.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_history_ruangan_list.adapter = historyRuanganAdapter

        getRuanganHistory()
    }

    private fun getRuanganHistory() {
        val client = ApiConfig.getApiService().getLastRuangan(5)

        client.enqueue(object : Callback<Ruangan> {
            override fun onResponse(call: Call<Ruangan>, response: Response<Ruangan>) {
                if (response.isSuccessful) {
                    val dataArray = response.body()?.data as List<DataRuangan>
                    for (data in dataArray) {
                        historyRuanganAdapter.addData(data)
                    }
                }
            }

            override fun onFailure(call: Call<Ruangan>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }
}