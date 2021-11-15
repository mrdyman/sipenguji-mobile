package com.diman.sipenguji.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.diman.sipenguji.LoginActivity
import com.diman.sipenguji.R
import com.diman.sipenguji.util.SharedPreferences
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_logout.setOnClickListener {
            //reset login data
            val sharePref = SharedPreferences(requireContext())
            sharePref.userSignature = ""
            val i = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(i)
            activity?.finish()
        }
    }
}