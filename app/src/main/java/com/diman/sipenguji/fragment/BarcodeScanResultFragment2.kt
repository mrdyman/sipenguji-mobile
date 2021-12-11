package com.diman.sipenguji.fragment

import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.diman.sipenguji.MainActivity
import com.diman.sipenguji.R
import com.diman.sipenguji.RuteTerpendekActivity
import com.diman.sipenguji.ScanActivity
import com.diman.sipenguji.adapter.GambarAdapter
import com.diman.sipenguji.adapter.RuanganAdapter
import com.diman.sipenguji.model.*
import com.diman.sipenguji.network.ApiConfig
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.bottom_sheet_scan.*
import kotlinx.android.synthetic.main.fragment_barcode_scan_result.*
import kotlinx.android.synthetic.main.fragment_barcode_scan_result2.*
import kotlinx.android.synthetic.main.fragment_barcode_scan_result2.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class BarcodeScanResultFragment2 : BottomSheetDialogFragment() {

    private lateinit var gambarAdapter: GambarAdapter
    var isNisnValid: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_barcode_scan_result2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        gambarAdapter = GambarAdapter(mutableListOf())
        rv_gambar_gedungxx.setHasFixedSize(true)
        rv_gambar_gedungxx.layoutManager = GridLayoutManager(activity, 2)
        rv_gambar_gedungxx.adapter = gambarAdapter

        //hide layout saat pertamakali tampil
        iv_barcode_scan_result?.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.man_search, activity?.theme))
        tv_scan_result_title?.text = "Yah! datanya tidak ditemukan -_-"
        cv_info_peserta?.visibility = View.INVISIBLE
        cv_info_jadwal?.visibility = View.INVISIBLE
        tv_foto_gedung_title?.visibility = View.INVISIBLE
        gambar_ruangan_container?.visibility = View.INVISIBLE
        rl_fab_scan_result?.visibility = View.INVISIBLE
    }

    private fun vibrate(){
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(500)
        }
    }

    //display data from API to bottomsheet
    fun displayData(kode: String) {
        getData(kode) { id, namaMahasiswa, nik, nisn, nomorUjian, jadwal, kelompok, ruangan, lokasiUjian, latitude, longitude ->
            view?.apply {
                //cek apakah data ditemukan?
                if (namaMahasiswa != null){
                    vibrate()
                    iv_barcode_scan_result?.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.found_routes, activity?.theme))
                    tv_scan_result_title.text = "Datanya Ketemu nih!"
                    cv_info_peserta.visibility = View.VISIBLE
                    cv_info_jadwal.visibility = View.VISIBLE
                    tv_foto_gedung_title.visibility = View.VISIBLE
                    gambar_ruangan_container.visibility = View.VISIBLE
                    rl_fab_scan_result.visibility = View.VISIBLE
                    Log.d("dimanandi", "nisn true")
                    tv_result_nama.text = namaMahasiswa
                    tv_result_nik.text = nik
                    tv_result_nisn.text = nisn.toString()
                    tv_result_nomor_ujian.text = nomorUjian
                    tv_result_jadwal.text = jadwal
                    tv_result_kelompok.text = kelompok
                    tv_result_ruangan_ujian.text = ruangan
                    tv_result_lokasi_ujian.text = lokasiUjian
                    findViewById<FloatingActionButton>(R.id.fab_get_rute).setOnClickListener {
                        //show activity jalur terpendek
                        Intent(Intent.ACTION_VIEW).also {
                            val i = Intent(activity, RuteTerpendekActivity::class.java)
                            //siapkan data untuk dikirim ke fragment maps
                            //ambil data latitude dan longitude dari scanActivity
                            val lat = (activity as ScanActivity).userLatitude
                            val lng = (activity as ScanActivity).userLongitude
                            Log.d("Loc", lat.toString())
                            i.putExtra("id_tujuan", id.toString())
                            i.putExtra("source_latitude", lat.toString())
                            i.putExtra("source_longitude", lng.toString())
                            i.putExtra("destination_latitude", latitude)
                            i.putExtra("destination_longitude", longitude)
                            i.putExtra("destination_name", ruangan)
                            startActivity(i)
                        }
                    }
                }
            }
        }
    }

    private fun getData(
        kode: String,
        callback: (id: Int?, namaMahasiswa: String?, nik: String?, nisn: String?, nomorUjian: String?, jadwal: String?, kelompok: String?, namaRuangan: String, lokasi: String?, latitude: String, longitude: String) -> Unit
    ) {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            //call sipenguji-api
            val client = ApiConfig.getApiService().getRuanganByNisn(kode)
            client.enqueue(object : Callback<RuanganDetail> {
                override fun onResponse(
                    call: Call<RuanganDetail>,
                    response: Response<RuanganDetail>
                ) {
                    if (response.isSuccessful) {
                        Log.i("Response", "Successful get data to API")
                        val data = response.body()?.data
                        if (data == null){
                            //data tidak ditemukan di server
                            iv_barcode_scan_result.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.man_search, activity?.theme))
                            tv_scan_result_title.text = "Datanya tidak ketemu nih!"
                            cv_info_peserta.visibility = View.GONE
                            cv_info_jadwal.visibility = View.GONE
                            tv_foto_gedung_title.visibility = View.GONE
                            gambar_ruangan_container.visibility = View.GONE
                            rl_fab_scan_result.visibility = View.GONE
                        }
                        isNisnValid = true
                        val id = data?.id.toString().toInt()
                        val namaMahasiswa = data?.nama.toString()
                        val nik = data?.nik.toString()
                        val nisn = data?.nisn.toString()
                        val nomorUjian = data?.nomorPeserta.toString()
                        val jadwal = data?.jadwal.toString()
                        val kelompok = data?.jenisUjian.toString()
                        val namaRuangan = data?.namaRuangan.toString()
                        val lokasi = data?.alamat.toString()
                        val latitude = data?.latitude.toString()
                        val longitude = data?.longitude.toString()

                            //call function getGambar untuk ambil
                            // data gambar ruangan dari server
                            getGambarRuangan(id)

                            //kirim data ke function getData()
                            handler.post {
                                callback(
                                    id,
                                    namaMahasiswa,
                                    nik,
                                    nisn,
                                    nomorUjian,
                                    jadwal,
                                    kelompok,
                                    namaRuangan,
                                    lokasi,
                                    latitude,
                                    longitude
                                )
                            }
                    } else {
                        Log.i("Response", response.message().toString())
                    }
                }

                override fun onFailure(call: Call<RuanganDetail>, t: Throwable) {
                    Log.i("Response", "Failed to connect to API ${t.printStackTrace()}")
                }

            })
        }
    }

    private fun getGambarRuangan(id: Int) {
        val client = ApiConfig.getApiService().getGambar(id)

        client.enqueue(object : Callback<GambarRuangan> {
            override fun onResponse(call: Call<GambarRuangan>, response: Response<GambarRuangan>) {
                if (response.isSuccessful) {
                    Log.d("Response", "Connecting to API Endpoint Gambar Successful")
                    Log.d("Response", "${response.body()?.data}")
                    val dataGambar = response.body()?.data as List<DataGambar>

                    for (data in dataGambar) {
                        gambarAdapter.addGambar(data)
                    }
                } else {
                    Log.d("Response", "Connecting to API Endpoint Ruangan Failed!")
                }
            }

            override fun onFailure(call: Call<GambarRuangan>, t: Throwable) {
                Log.e("Response", "Failed connecting to API with message ${t.printStackTrace()}")
            }

        })

    }
}