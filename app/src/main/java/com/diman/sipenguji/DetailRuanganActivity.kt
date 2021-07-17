package com.diman.sipenguji

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.diman.sipenguji.fragment.DetailGedungFragment
import com.diman.sipenguji.fragment.DetailRuanganFragment

class DetailRuanganActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_ruangan)

        val fr = supportFragmentManager.beginTransaction()
        fr.add(R.id.ruangan_detail_container, DetailRuanganFragment())
        fr.commit()
    }
}