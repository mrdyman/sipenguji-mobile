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


        //display polyline from API
        val polyline = gMap.addPolyline(PolylineOptions().addAll(dataPolyline))
        polyline.color = (requireActivity().getColor(R.color.orange))
        polyline.width = 12f

        //add marker at start and end of polyline
        val origin = dataPolyline[0]
        val destination = dataPolyline[dataPolyline.size - 1]
        gMap.addMarker(MarkerOptions().position(origin).title("Your Location"))
        gMap.addMarker(MarkerOptions().position(destination).title("Your Destination").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)))

        //zoom camera maps to destination
        gMap.moveCamera(CameraUpdateFactory.newLatLng(destination))
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destination, 17.0f))
    }
}