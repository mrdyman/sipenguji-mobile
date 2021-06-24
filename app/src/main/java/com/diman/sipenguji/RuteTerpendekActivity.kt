package com.diman.sipenguji

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.diman.sipenguji.fragment.LocationFragment
import com.diman.sipenguji.fragment.MapsFragment
import com.diman.sipenguji.model.Calculate
import com.google.android.gms.maps.model.LatLng
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.PendingResult
import com.diman.sipenguji.network.ApiConfig
import com.google.maps.internal.PolylineEncoding
import com.google.maps.model.DirectionsResult
import kotlinx.android.synthetic.main.fragment_location.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RuteTerpendekActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rute_terpendek)

        val fr = supportFragmentManager.beginTransaction()
        fr.add(R.id.ll_container_maps, MapsFragment())
        fr.add(R.id.ll_location_container, LocationFragment())
        fr.commit()

        calculateDirections()
    }

    private fun calculateDirections() {
        var mGeoApiContext: GeoApiContext? = null
        Log.d("calculateDirections:","calculating directions.")

        val sourceLat = intent.getStringExtra("source_latitude")
        val sourceLng = intent.getStringExtra("source_longitude")

        val gerbangLat : Double = -0.8361183597555601
        val gerbangLng : Double = 119.88954164333798

        val destination = com.google.maps.model.LatLng(gerbangLat, gerbangLng)
        mGeoApiContext = GeoApiContext.Builder()
            .apiKey(getString(R.string.google_maps_api_key))
            .build()
        val directions = DirectionsApiRequest(mGeoApiContext)
        directions.alternatives(false)
        directions.origin(com.google.maps.model.LatLng(sourceLat.toDouble(),sourceLng.toDouble()))

        Log.d("calculateDirections:","destination: $destination")

        directions.destination(destination)
            .setCallback(object : PendingResult.Callback<DirectionsResult> {
                override fun onResult(result: DirectionsResult) {
                    Log.d("calculateDirections:","routes: $result")
                    Log.d("Directions:routes:", "${result.routes[0]}");
                    Log.d("Directions Duration", "${result.routes[0].legs[0].duration}")
                    Log.d("Directions Distance", "${result.routes[0].legs[0].distance}")
                    Log.d("DirectiongeocodWayPoint", "${result.geocodedWaypoints[0]}")
                    Log.d("onResult:","successfully retrieved directions. from user -> gerbang")
                    decodePolyline(result)
                }

                override fun onFailure(e: Throwable) {
                    Log.e("calculateDirections:","Failed to get directions: " + e.message)
                }
            })
    }

    private fun decodePolyline(result: DirectionsResult) {
        Handler(Looper.getMainLooper()).post {
            val newDecodedPath: MutableList<LatLng> = ArrayList()
            Log.d("run: result routes: ", "${result.routes.size}")
            for (route in result.routes) {
                Log.d("run: leg: ", "${route.legs[0]}")
                val decodedPath = PolylineEncoding.decode(route.overviewPolyline.encodedPath)

                // This loops through all the LatLng coordinates of ONE polyline.
                for (latLng in decodedPath) {
                    newDecodedPath.add(LatLng(latLng.lat, latLng.lng))
                }
            }
            Log.i("newDecodedPath", "$newDecodedPath")

            //call function API dan kirim hasil decode polyline
            Calculate(newDecodedPath.toString())
        }
    }

    private fun Calculate(newDecodedPath: String){
        Log.i("dataPath", newDecodedPath)
        //ambil lokasi user
        val sourceLat = intent.getStringExtra("source_latitude")
        val sourceLng = intent.getStringExtra("source_longitude")

        //ambil lokasi tujuan
        val idTujuan = intent.getStringExtra("id_tujuan")
        val destinationLat = intent.getStringExtra("destination_latitude")
        val destinationLng = intent.getStringExtra("destination_longitude")
        //gabungkan koordinat jadi LatLng
        val sourceLatLng = "$sourceLat, $sourceLng"
        val destinationLatLng = "$destinationLat, $destinationLng"

        //call API
        val client = ApiConfig.getApiService().calculate(sourceLatLng, idTujuan, newDecodedPath)
        client.enqueue(object : Callback<Calculate>{
            override fun onResponse(call: Call<Calculate>, response: Response<Calculate>) {
                if(response.isSuccessful){
                    Log.i("ResponseCalculate", "Succesfull with data: ${response.body()} ")
                } else {
                    Log.i("ResponseCalculate", "Request to API Unsuccessful")
                }
            }

            override fun onFailure(call: Call<Calculate>, t: Throwable) {
                Log.e("Response", "Failed to get decoded data from API with message: ${t.printStackTrace()}")
            }
        })
        // request direction sekali lagi dari lokasi user -> gedung
        //modifikasi polyline -> gantikan dengan polyline dari API
        //tampilkan ke user
    }
}