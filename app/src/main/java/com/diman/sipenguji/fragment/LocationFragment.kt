package com.diman.sipenguji.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.diman.sipenguji.R
import kotlinx.android.synthetic.main.fragment_location.*

class LocationFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val srcLatitude = activity?.intent?.getStringExtra("source_latitude")
        val srcLongitude = activity?.intent?.getStringExtra("source_longitude")
        val user_koordinat = "$srcLatitude, $srcLongitude"
        tv_source.text = user_koordinat
        tv_destination.text = activity?.intent?.getStringExtra("destination_name")
    }

}