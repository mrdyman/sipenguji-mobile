package com.diman.sipenguji.fragment

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.transition.Visibility
import com.diman.sipenguji.BuildConfig
import com.diman.sipenguji.R
import com.diman.sipenguji.model.DataItem
import com.diman.sipenguji.model.Gedung
import com.diman.sipenguji.network.ApiConfig
import com.diman.sipenguji.network.UploadRequestBody
import com.diman.sipenguji.util.getFileName
import com.diman.sipenguji.util.snackbar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_upload_image.*
import kotlinx.android.synthetic.main.fragment_add_data.*
import kotlinx.android.synthetic.main.fragment_add_data.et_alamat_gedung
import kotlinx.android.synthetic.main.fragment_add_data.et_nama_gedung
import kotlinx.android.synthetic.main.layout_add_data.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class AddDataFragment : Fragment(), UploadRequestBody.UploadCallback {

    private var selectedImage: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_data, container, false)
    }

    override fun onProgressUpdate(percentage: Int) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cv_gedungSelected.visibility = View.VISIBLE

        cv_data_gedung.setOnClickListener {
            cv_gedungSelected.visibility = View.VISIBLE
            cv_ruanganSelected.visibility = View.INVISIBLE

            ll_form_gedung_root.visibility = View.VISIBLE
            ll_form_ruangan_root.visibility = View.GONE
        }

        cv_data_ruangan.setOnClickListener {
            cv_ruanganSelected.visibility = View.VISIBLE
            cv_gedungSelected.visibility = View.INVISIBLE

            ll_form_gedung_root.visibility = View.GONE
            ll_form_ruangan_root.visibility = View.VISIBLE

            showListAlamat()
        }

        var latestTmpUri: Uri? = null

        val uploadFromGallery = registerForActivityResult(ActivityResultContracts.GetContent()) {
            iv_upload_pict_gedung.setImageURI(it)
            cv_placeholderHint_gedung.visibility = View.GONE
            //assign uri ke variable selectedImage
            selectedImage = it
        }

        val uploadFromCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                latestTmpUri?.let { uri ->
                    iv_upload_pict_gedung.setImageURI(uri)
                    cv_placeholderHint_gedung.visibility = View.GONE
                    //assign uri ke variable selectedImage
                    selectedImage = uri
                }
            }
        }

        iv_upload_pict_gedung.setOnClickListener {
            val builder = AlertDialog.Builder(activity)
            builder.setView(R.layout.dialog_upload_image)

            val dialog: AlertDialog = builder.create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)
            dialog.show()

            dialog.cv_upload_fromGallery.setOnClickListener {
                dialog.dismiss()
                uploadFromGallery.launch("image/*")
            }

            dialog.cv_upload_fromCamera.setOnClickListener {
                dialog.dismiss()
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                    takePictureIntent.resolveActivity(requireActivity().packageManager).apply {
                        val permission: Int = ContextCompat.checkSelfPermission(
                            requireActivity(),
                            android.Manifest.permission.CAMERA
                        )
                        if (permission != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(
                                requireActivity(),
                                arrayOf(android.Manifest.permission.CAMERA),
                                1
                            )
                        } else {
                            lifecycleScope.launchWhenStarted {
                                getTmpFileUri().let { uri ->
                                    latestTmpUri = uri
                                    uploadFromCamera.launch(uri)
                                }
                            }
                        }
                    }

                }
            }

            dialog.btn_close_dialog_uploadImage.setOnClickListener {
                dialog.dismiss()
            }
        }

        btn_save_data.setOnClickListener {
            val formGedung = ll_form_gedung_root
            val formRuangan = ll_form_ruangan_root
            if (formGedung.visibility == View.GONE && formRuangan.visibility == View.VISIBLE){
                addNewDataRuangan()
            } else if(formGedung.visibility == View.VISIBLE && formRuangan.visibility == View.GONE){
                addNewDataGedung()
            } else {
                add_data_root.snackbar("Data yang ingin di input tidak diketahui")
            }
        }
    }

    private fun showListAlamat(){
        val dataAlamat : MutableList<String> = ArrayList()
        val dataId : MutableList<Int> = ArrayList()
        val client = ApiConfig.getApiService().getListGedung()
        client.enqueue(object : Callback<Gedung>{
            override fun onResponse(call: Call<Gedung>, response: Response<Gedung>) {
                if (response.isSuccessful){
                    val dataArray = response.body()?.data as List<DataItem>
                    for (data in dataArray) {
                        dataAlamat.add(data.namaGedung!!)
                        dataId.add(data.id!!.toInt())
                    }
                    val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_alamat_ruangan, dataAlamat)
                    val tvAlamat = tv_alamatRuangan
                    tvAlamat.setAdapter(adapter)
                    tvAlamat.setOnItemClickListener { parent, view, position, id ->
                        val idGedung = dataId[position]
                        Log.d("GedungPosition", idGedung.toString())
                    }
                } else {
                    add_data_root.snackbar(response.message())
                }
            }

            override fun onFailure(call: Call<Gedung>, t: Throwable) {
                add_data_root.snackbar(t.message.toString())
            }

        })
    }

    private fun addNewDataGedung() {
        val namaGedung = et_nama_gedung.text.toString().trim()
        val alamatGedung = et_alamat_gedung.text.toString().trim()
        if (selectedImage == null) {
            //image not selected yet
                add_data_root.snackbar("Mohon pilih gambar gedung")
            return
        }

        val parcelFileDescriptor =
            activity?.contentResolver?.openFileDescriptor(selectedImage!!, "r", null) ?: return
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(
            requireActivity().cacheDir,
            requireActivity().contentResolver.getFileName(selectedImage!!)
        )
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        val body = UploadRequestBody(file, "image", this)

        val client = ApiConfig.getApiService().createGedung(
            namaGedung.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            alamatGedung.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            MultipartBody.Part.createFormData("img", file.name, body),
            "Latitude".toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "Longitude".toRequestBody("multipart/form-data".toMediaTypeOrNull())
        )

        client.enqueue(object : Callback<Gedung> {
            override fun onResponse(call: Call<Gedung>, response: Response<Gedung>) {
                add_data_root.snackbar("Data gedung berhasil ditambahkan")
            }

            override fun onFailure(call: Call<Gedung>, t: Throwable) {
                add_data_root.snackbar(t.message.toString())
            }

        })
    }

    private fun addNewDataRuangan(){
        val namaRuangan = et_nama_ruangan.text.toString().trim()
        val alamatRuangan = tv_alamatRuangan.text.toString().trim()
        val jumlahPeserta = et_jumlah_peserta.text.toString().trim()
        val latitude = et_latitude.text.toString().trim()
        val longitude = et_longitude.text.toString().trim()
        if (selectedImage == null) {
            //image not selected yet
            add_data_root.snackbar("Mohon pilih gambar ruangan")
            return
        }

        val parcelFileDescriptor =
            activity?.contentResolver?.openFileDescriptor(selectedImage!!, "r", null) ?: return
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(
            requireActivity().cacheDir,
            requireActivity().contentResolver.getFileName(selectedImage!!)
        )
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        val body = UploadRequestBody(file, "image", this)

        val client = ApiConfig.getApiService().(
            namaGedung.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            alamatGedung.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            MultipartBody.Part.createFormData("img", file.name, body),
            "Latitude".toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "Longitude".toRequestBody("multipart/form-data".toMediaTypeOrNull())
        )

        client.enqueue(object : Callback<Gedung> {
            override fun onResponse(call: Call<Gedung>, response: Response<Gedung>) {
                add_data_root.snackbar("Data gedung berhasil ditambahkan")
            }

            override fun onFailure(call: Call<Gedung>, t: Throwable) {
                add_data_root.snackbar(t.message.toString())
            }

        })
    }

    private fun getTmpFileUri(): Uri {
        val cacheDir: File? = null;
        val tmpFile = File.createTempFile("tmp_image_file", ".png", cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(
            requireActivity(),
            "${BuildConfig.APPLICATION_ID}.provider",
            tmpFile
        )
    }
}