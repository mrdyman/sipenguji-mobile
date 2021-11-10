package com.diman.sipenguji

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.diman.sipenguji.fragment.DetailGedungFragment
import com.diman.sipenguji.fragment.HomeFragment
import kotlinx.android.synthetic.main.fragment_detail_gedung.*

class DetailGedungActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_gedung)

        val fr = supportFragmentManager.beginTransaction()
        fr.add(R.id.gedung_detail_container, DetailGedungFragment())
        fr.commit()
    }
}