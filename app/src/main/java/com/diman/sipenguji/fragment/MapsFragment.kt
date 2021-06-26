package com.diman.sipenguji.fragment

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.diman.sipenguji.R
import com.diman.sipenguji.RuteTerpendekActivity
import com.google.android.gms.location.FusedLocationProviderClient

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.PendingResult
import com.google.maps.internal.PolylineEncoding
import com.google.maps.model.DirectionsResult

class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var dataPolyline : MutableList<LatLng>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("Now", "Creating Fragment")
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Now", "Fragment Created")
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(gMap: GoogleMap) {
        Log.d("Now", "calling fun onMapReady")
        dataPolyline = (activity as RuteTerpendekActivity).direction
        Log.d("Now", dataPolyline.toString())

        val kos = LatLng(-0.8306791,119.8849217)
        gMap.addMarker(MarkerOptions().position(kos).title("Marker in Kos"))
        gMap.moveCamera(CameraUpdateFactory.newLatLng(kos))
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(kos, 17.0f))

        gMap.addPolyline(PolylineOptions().addAll(dataPolyline))
    }
}