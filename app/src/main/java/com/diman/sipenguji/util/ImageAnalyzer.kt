package com.diman.sipenguji.util

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.fragment.app.FragmentManager
import com.diman.sipenguji.fragment.BarcodeScanResultFragment
import com.diman.sipenguji.fragment.BarcodeScannerFragment
import com.diman.sipenguji.model.Gedung
import com.diman.sipenguji.network.ApiConfig
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class ImageAnalyzer(private val fragementManager: FragmentManager): ImageAnalysis.Analyzer {
    private var bottomsheet = BarcodeScanResultFragment()

    override fun analyze(imageProxy: ImageProxy) {
        scanBarcode(imageProxy)
    }

    @SuppressLint("UnsafeExperimentalUsageError", "UnsafeOptInUsageError")
    private fun scanBarcode(imageProxy: ImageProxy) {
        imageProxy.image?.let { image ->
            val inputImage = InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)
            val scanner = BarcodeScanning.getClient()
            scanner.process(inputImage)
                .addOnCompleteListener {
                    imageProxy.close()
                    if (it.isSuccessful) {
                        readBarcodeData(it.result as List<Barcode>)
                    } else {
                        it.exception?.printStackTrace()
                    }
                }
        }
    }

    private fun readBarcodeData(barcodes: List<Barcode>) {
        for (barcode in barcodes) {
            when (barcode.valueType) {
                //you can check if the barcode has other values
                //For now I am using it just for URL
                Barcode.TYPE_URL -> {
                    //we have the URL here
                    val url = barcode.url?.url
                    if (!bottomsheet.isAdded){
                        bottomsheet.show(fragementManager, "")
                        bottomsheet.updateURL(url.toString())
                    }
                }
                Barcode.TYPE_TEXT -> {
                    val kode = barcode.displayValue
                    Log.i("QRCODE_RESULT", kode)
                    //show fragment
                    if (!bottomsheet.isAdded) {
                        //call sipenguji-api
                        bottomsheet.show(fragementManager, "")
                        bottomsheet.displayData(kode.toInt())
                    }
                }
            }
        }
    }
}