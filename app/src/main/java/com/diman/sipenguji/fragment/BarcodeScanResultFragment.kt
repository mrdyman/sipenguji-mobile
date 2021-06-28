package com.diman.sipenguji.fragment

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.diman.sipenguji.MainActivity
import com.diman.sipenguji.R
import com.diman.sipenguji.RuteTerpendekActivity
import com.diman.sipenguji.model.Gedung
import com.diman.sipenguji.model.Ruangan
import com.diman.sipenguji.network.ApiConfig
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_barcode_scan_result.*
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors
import java.util.jar.Manifest

class BarcodeScanResultFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_barcode_scan_result, container, false)
    }

    //we will call this function to update the URL displayed
    fun updateURL(url: String){
        fetchUrlMetaData(url){ title, desc ->
            view?.apply {
                findViewById<TextView>(R.id.text_view_title)?.text = title
                findViewById<TextView>(R.id.text_view_desc)?.text = desc
                findViewById<TextView>(R.id.text_view_link)?.text = url
                findViewById<TextView>(R.id.text_view_visit_link).setOnClickListener { _ ->
                    Intent(Intent.ACTION_VIEW).also {
                        it.data = Uri.parse(url)
                        startActivity(it)
                    }
                }
            }
        }
    }

    //this function will fetch URL data
    private fun fetchUrlMetaData(url: String, callback: (title: String, desc: String) -> Unit){
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute{
            val doc = Jsoup.connect(url).get()
            val desc = doc.select("meta[name=description]")[0].attr("content")
            handler.post{
                callback(doc.title(), desc)
            }
        }
    }

    //display data from API to bottomsheet
    fun displayData(kode: Int){
        getData(kode){ namaMahasiswa, nomorUjian, jadwalUjian, id, namaGedung, alamat, latitude, longitude ->
            view?.apply {
                tv_value_nama.text = namaMahasiswa
                tv_value_nomor_ujian.text = nomorUjian
                tv_value_jadwal.text = jadwalUjian
                tv_value_gedung.text = namaGedung
                tv_value_alamat.text = alamat
                findViewById<Button>(R.id.btn_get_rute).setOnClickListener {
                    //show activity jalur terpendek
                    Intent(Intent.ACTION_VIEW).also {
                        val i = Intent(activity, RuteTerpendekActivity::class.java)
                        //siapkan data untuk dikirim ke fragment maps
                        //ambil data latitude dan longitude dari mainActivity
                        val lat = (activity as MainActivity).userLatitude
                        val lng = (activity as MainActivity).userLongitude
                        Log.d("Loc", lat.toString())
                        i.putExtra("id_tujuan", id)
                        i.putExtra("source_latitude", lat.toString())
                        i.putExtra("source_longitude", lng.toString())
                        i.putExtra("destination_latitude", latitude)
                        i.putExtra("destination_longitude", longitude)
                        i.putExtra("destination_name", namaGedung)
                        startActivity(i)
                    }
                }
            }
        }

    }

    private fun getData(kode: Int, callback: (namaMahasiswa: String, nomorUjian: String, jadwalUjian: String, id: String, namaGedung: String, alamat: String, latitude: String, longitude: String) -> Unit ){
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute{
            //call sipenguji-api
            val client = ApiConfig.getApiService().getRuangan(kode)
            client.enqueue(object: Callback<Ruangan>{
                override fun onResponse(call: Call<Ruangan>, response: Response<Ruangan>) {
                    if (response.isSuccessful){
                        Log.i("Response", "Successful get data to API")
                        val data = response.body()?.data?.get(0)
                        val namaMahasiswa = "A. Mardiman Saputra" //will change to data from api
                        val nomorUjian = "3123123123123123"
                        val jadwalUjian = data?.jadwal.toString()
                        val id = data?.id.toString()
                        val namaGedung = data?.namaGedung.toString()
                        val alamat = data?.alamat.toString()
                        val latitude = data?.latitude.toString()
                        val longitude = data?.longitude.toString()

                        //kirim data ke function getData()
                        handler.post {
                            callback(namaMahasiswa, nomorUjian, jadwalUjian, id, namaGedung, alamat, latitude, longitude)
                        }
                    } else {
                        Log.i("Response", response.message().toString())
                    }
                }

                override fun onFailure(call: Call<Ruangan>, t: Throwable) {
                    Log.i("Response", "Failed to connect to API ${t.printStackTrace()}")
                }

            })
        }
    }
}