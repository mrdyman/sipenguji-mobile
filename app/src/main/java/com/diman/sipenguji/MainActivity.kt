package com.diman.sipenguji

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.diman.sipenguji.fragment.BarcodeScannerFragment
import com.diman.sipenguji.fragment.HomeFragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.facebook.stetho.Stetho
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQ_CODE = 1000
    var userLatitude: Double = 0.0
    var userLongitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //check location permission
        checkLocationPermission()

        //getUserLocation
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getUserLocation()

        Stetho.initializeWithDefaults(this);

        bottomNavigation.show(1, true)
        bottomNavigation.add(MeowBottomNavigation.Model(1, R.drawable.ic_home))
        bottomNavigation.add(MeowBottomNavigation.Model(2, R.drawable.ic_qrcode))
        bottomNavigation.add(MeowBottomNavigation.Model(3, R.drawable.ic_message))

        val fr = supportFragmentManager.beginTransaction()
        fr.add(R.id.fragment_container, HomeFragment())
        fr.commit()

        bottomNavigation.setOnClickMenuListener {
            var selectedFragment: Fragment;
            when(it.id){
                1 -> {
                    Log.i("Message", "Home Selected")
                    selectedFragment = HomeFragment()
                    replaceFragment(selectedFragment)
                }

                2 -> {
                    Log.i("Message", "Scanner Selected")
                    selectedFragment = BarcodeScannerFragment()
                    replaceFragment(selectedFragment)
                }

                3 -> {
                    Log.i("Message", "Menu Selected")
//                    selectedFragment = HomeFragment()
                }

            }
        }
    }

    private fun replaceFragment(frNama: Fragment){
        val _fragment = supportFragmentManager.beginTransaction()
        _fragment.replace(R.id.fragment_container, frNama)
        _fragment.commit()
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
            checkLocationPermission()
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

    private fun checkLocationPermission(){
        //cek location permission
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Log.d("UserLocation", "Location permission not granted,")
            //requestLocation
            makeRequest()

        } else {
            Log.d("UserLocation", "Location permission granted,")
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_PERMISSION_REQ_CODE)
    }
}