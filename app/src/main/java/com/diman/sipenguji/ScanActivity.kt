package com.diman.sipenguji

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Size
import android.view.View
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.diman.sipenguji.adapter.RuanganAdapter
import com.diman.sipenguji.model.DataRuangan
import com.diman.sipenguji.model.Ruangan
import com.diman.sipenguji.network.ApiConfig
import com.diman.sipenguji.util.ImageAnalyzer
import com.google.android.gms.location.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.android.synthetic.main.activity_scan.*
import kotlinx.android.synthetic.main.bottom_sheet_scan.*
import kotlinx.android.synthetic.main.dialog_failed.*
import kotlinx.android.synthetic.main.dialog_help.*
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ScanActivity : AppCompatActivity() {

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var analyzer: ImageAnalyzer
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<CardView>
    private lateinit var ruanganAdapter: RuanganAdapter
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var userLatitude: Double = 0.0
    var userLongitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        //check permission (location and camera)
        checkPermission()

        //getUserLocation
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getUserLocation()

        cameraExecutor = Executors.newSingleThreadExecutor()
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        analyzer = ImageAnalyzer(this.supportFragmentManager)

        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(this))

        //listener for view (button, image, textview) when clicked
        viewListener()
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider){
            val preview: Preview = Preview.Builder().build()
            val cameraSelector: CameraSelector = CameraSelector.Builder().requireLensFacing(
                CameraSelector.LENS_FACING_BACK
            ).build()
            preview.setSurfaceProvider(scan_preview.surfaceProvider)
            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
            imageAnalysis.setAnalyzer(cameraExecutor, analyzer)
            cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, imageAnalysis, preview)
    }

    private fun getUserLocation() {
        //request lokasi user
        val locationRequest = LocationRequest.create().apply {
            interval = 5000
            fastestInterval = 3000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            maxWaitTime = 2000
        }

        Log.d("Location", "Getting location information")
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            checkPermission()
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                val location = locationResult.lastLocation
                if (location != null){
                    userLatitude = location.latitude
                    userLongitude = location.longitude
                } else {
                    Log.d("UserLocation", "Failed to get user location")
                }
                Log.d("UserLocation", "Latitude: ${userLatitude}, Longitude: ${userLongitude}")
            }
        }, Looper.myLooper())
    }

    private fun checkPermission(){
        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)

        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            Log.e("Error", "Camera Permission not GRANTED!")
            makeRequest("camera")
        }

        if (locationPermission != PackageManager.PERMISSION_GRANTED){
            Log.d("Error", "Location Permission not Granted")
            makeRequest("location")
        }
    }

    private fun makeRequest(permissionName : String){
        if (permissionName == "camera"){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),
                CAMERA_REQ
            )
        } else if (permissionName == "location"){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_REQ
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQ -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        "You need the camera permission to use this app",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            LOCATION_REQ -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        "You need the camera permission to use this app",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun viewListener(){
        ruanganAdapter = RuanganAdapter(mutableListOf())
        rv_ruangan_ujian.setHasFixedSize(true)
        rv_ruangan_ujian.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_ruangan_ujian.adapter = ruanganAdapter

        //set bottom sheet behaviour (make bottom sheet draggable)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomsheet_scan)

        et_namaRuangan.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //sebelum edit text berubah
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("didimaman", "Edit Text Changed to $s")
                if (et_namaRuangan.text.toString().trim().isNotBlank()){
                    getRuangan(s.toString())
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    list_tips_scan.visibility = View.GONE
                } else {
                    //clear data pada recyclerView
                    ruanganAdapter.clearDataRuangan()
                    list_tips_scan.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //setelah edit text berubah
            }

        })

        iv_help.setOnClickListener {
            showHelpDialog()
        }
    }

    private fun showHelpDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setView(R.layout.dialog_help)

        val dialog: AlertDialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.show()

        dialog.btn_close_dialog_help_scan.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun getRuangan(namaRuangan : String){
        val client = ApiConfig.getApiService().getRuanganByName(namaRuangan)

        client.enqueue(object : Callback<Ruangan> {
            override fun onResponse(call: Call<Ruangan>, response: Response<Ruangan>) {
                if (response.isSuccessful){
                    Log.d("Response", "Connecting to API Endpoint Ruangan Successful")
                    Log.d("Response", "${response.body()?.data}")
                    val dataRuangan = response.body()?.data as List<DataRuangan>
                    //clear data pada recyclerView
                    ruanganAdapter.clearDataRuangan()
                    for (data in dataRuangan){
                        ruanganAdapter.addDataRuangan(data)
                        Log.d("didimaman", "loop ${dataRuangan.size}")
                    }
                } else {
                    //clear data pada recyclerView
                    ruanganAdapter.clearDataRuangan()
                    Log.d("Response", "Connecting to API Endpoint Ruangan Failed!")
                }
            }

            override fun onFailure(call: Call<Ruangan>, t: Throwable) {
                Log.e("Response", "Failed connecting to API with message ${t.printStackTrace()}")
            }

        })
    }

    companion object {
        private const val CAMERA_REQ = 101
        private const val LOCATION_REQ = 102
    }
}