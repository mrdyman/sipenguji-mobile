package com.diman.sipenguji

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.diman.sipenguji.fragment.HomeFragment
import com.diman.sipenguji.fragment.MapsFragment

class RuteTerpendekActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rute_terpendek)

        val fr = supportFragmentManager.beginTransaction()
        fr.add(R.id.ll_container_maps, MapsFragment())
        fr.commit()
    }
}