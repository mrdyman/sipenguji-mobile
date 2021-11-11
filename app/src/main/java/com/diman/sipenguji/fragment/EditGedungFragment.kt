package com.diman.sipenguji.fragment

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.diman.sipenguji.R
import com.diman.sipenguji.model.GedungDetail
import com.diman.sipenguji.network.ApiConfig
import com.diman.sipenguji.util.snackbar
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_edit_gedung.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditGedungFragment (private val idGedung: Int) : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_gedung, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val client = ApiConfig.getApiService().getGedung(idGedung)
        client.enqueue(object : Callback<GedungDetail>{
            override fun onResponse(call: Call<GedungDetail>, response: Response<GedungDetail>) {
                if (response.isSuccessful){
                    val data = response.body()?.data
                    et_nama_gedung_edit.text = Editable.Factory.getInstance().newEditable(data?.namaGedung)
                    et_alamat_gedung_edit.text = Editable.Factory.getInstance().newEditable(data?.alamat)
                } else {
                    ll_form_gedung_edit_root.snackbar(response.message())
                }
            }

            override fun onFailure(call: Call<GedungDetail>, t: Throwable) {
                ll_form_gedung_edit_root.snackbar(t.printStackTrace().toString())
            }

        })
    }
}