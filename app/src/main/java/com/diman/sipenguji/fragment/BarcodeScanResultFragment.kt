package com.diman.sipenguji.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ProxyFileDescriptorCallback
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.diman.sipenguji.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.jsoup.Jsoup
import java.util.concurrent.Executors

class BarcodeScanResultFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_barcode_scan_result, container, false)
    }

    //we will call this function to update the URL displayed
    fun updateURL(url: String){
        fetchUrlMetaData(url){ title, desc ->
            view?.apply {
                findViewById<TextView>(R.id.text_view_title)?.text = title
                findViewById<TextView>(R.id.text_view_desc)?.text = desc
                findViewById<TextView>(R.id.text_view_link)?.text = url
                findViewById<TextView>(R.id.text_view_visit_link).setOnClickListener { _ ->
                    Intent(Intent.ACTION_VIEW).also {
                        it.data = Uri.parse(url)
                        startActivity(it)
                    }
                }
            }
        }
    }

    //this function will fetch URL data
    private fun fetchUrlMetaData(url: String, callback: (title: String, desc: String) -> Unit){
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute{
            val doc = Jsoup.connect(url).get()
            val desc = doc.select("meta[name=description]")[0].attr("content")
            handler.post{
                callback(doc.title(), desc)
            }
        }
    }
}