package com.diman.sipenguji

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.diman.sipenguji.adapter.GedungAdapter
import com.diman.sipenguji.fragment.BarcodeScannerFragment
import com.diman.sipenguji.fragment.HomeFragment
import com.diman.sipenguji.model.DataItem
import com.diman.sipenguji.model.Gedung
import com.diman.sipenguji.network.ApiConfig
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.facebook.stetho.Stetho
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_gedung.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
}