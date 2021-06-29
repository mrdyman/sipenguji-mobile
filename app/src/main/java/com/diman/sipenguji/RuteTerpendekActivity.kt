package com.diman.sipenguji

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.diman.sipenguji.fragment.LocationFragment
import com.diman.sipenguji.fragment.MapsFragment
import com.diman.sipenguji.model.Calculate
import com.diman.sipenguji.network.ApiConfig
import com.google.android.gms.maps.model.LatLng
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.PendingResult
import com.google.maps.internal.PolylineEncoding
import com.google.maps.model.DirectionsResult
import kotlinx.android.synthetic.main.activity_rute_terpendek.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RuteTerpendekActivity : AppCompatActivity() {

    var finalPolyline : List<List<Double>>? = null
    var direction: MutableList<LatLng> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rute_terpendek)

        calculateDirections()

        initBtn()
    }

    private fun initBtn(){
        btn_get_direction.setOnClickListener {
            val lat = intent.getStringExtra("destination_latitude")
            val lng = intent.getStringExtra("destination_longitude")
            //call navigate function
            navigate(lat!!.toDouble(), lng!!.toDouble())
        }
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
        directions.origin(com.google.maps.model.LatLng(sourceLat.toDouble(), sourceLng.toDouble()))

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
            calculate(newDecodedPath.toString())
        }
    }

    private fun calculate(newDecodedPath: String){
        //ambil lokasi user
        val sourceLat = intent.getStringExtra("source_latitude")
        val sourceLng = intent.getStringExtra("source_longitude")

        //ambil lokasi tujuan
        val idTujuan = intent.getStringExtra("id_tujuan")

        //gabungkan koordinat jadi LatLng
        val sourceLatLng = "$sourceLat, $sourceLng"

        //call API
        val client = ApiConfig.getApiService().calculate(sourceLatLng, idTujuan, newDecodedPath)
        client.enqueue(object : Callback<Calculate>{
            override fun onResponse(call: Call<Calculate>, response: Response<Calculate>) {
                if(response.isSuccessful){
                    Log.i("ResponseCalculate", "Succesfull with data: ${response.body()}")
                    val data = response.body()?.data
                    finalPolyline  = data
                    Log.i("finalPolyline", finalPolyline.toString())
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
        requestFinalRoutes() // langsung call function display direction(tes kata)
    }

    private fun requestFinalRoutes(){
        var mGeoApiContext: GeoApiContext? = null
        Log.d("calculateDirections:","calculating final directions.")

        val sourceLat = intent.getStringExtra("source_latitude")
        val sourceLng = intent.getStringExtra("source_longitude")

        val destinationLat = intent.getStringExtra("destination_latitude")
        val destinationLng = intent.getStringExtra("destination_longitude")

        val destination = com.google.maps.model.LatLng(destinationLat.toDouble(), destinationLng.toDouble())

        mGeoApiContext = GeoApiContext.Builder()
            .apiKey(getString(R.string.google_maps_api_key))
            .build()
        val directions = DirectionsApiRequest(mGeoApiContext)

        directions.alternatives(false)

        directions.origin(com.google.maps.model.LatLng(sourceLat.toDouble(),sourceLng.toDouble()))
        directions.destination(destination)

            .setCallback(object : PendingResult.Callback<DirectionsResult> {
                override fun onResult(result: DirectionsResult) {
                    Log.d("calculateDirections:","routes: $result")
                    Log.d("Directions:routes:", "${result.routes[0]}");
                    Log.d("Directions Duration", "${result.routes[0].legs[0].duration}")
                    Log.d("Directions Distance", "${result.routes[0].legs[0].distance}")
                    Log.d("DirectiongeocodWayPoint", "${result.geocodedWaypoints[0]}")
                    Log.d("onResult:","successfully retrieved directions. from user -> gedung ujian")
                    displayDirection(result)
                }

                override fun onFailure(e: Throwable) {
                    Log.e("calculateDirections:","Failed to get final directions: " + e.message)
                }
            })
    }

    private fun displayDirection(data: DirectionsResult){
        for (poly in finalPolyline!!.indices) {
            Log.d("Looping Ke:${poly}", finalPolyline!![poly].toString())
            direction.add(
                LatLng(finalPolyline!![poly][0], finalPolyline!![poly][1])
            )
        }
        Log.i("PushResult", direction.toString())

        //display fragment maps and location maps fragment
        val fr = supportFragmentManager.beginTransaction()
        fr.add(R.id.ll_container_maps, MapsFragment())
        fr.add(R.id.ll_location_container, LocationFragment())
        fr.commit()
    }

    private  fun navigate(lat: Double, lng: Double){
        val intentUri = Uri.parse("google.navigation:q=$lat,$lng")
        val mapIntent = Intent(Intent.ACTION_VIEW, intentUri)
        mapIntent.setPackage("com.google.android.apps.maps")

        try {
            if (mapIntent.resolveActivity(this.packageManager) != null) {
                startActivity(mapIntent)
            }
        } catch (e: NullPointerException) {
            Log.e("Message", "Couldn't open map with message : ${e.message}")
            Toast.makeText(this, "Couldn't open map", Toast.LENGTH_SHORT).show()
        }
    }
}