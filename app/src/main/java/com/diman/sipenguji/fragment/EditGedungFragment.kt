package com.diman.sipenguji.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.diman.sipenguji.BuildConfig
import com.diman.sipenguji.R
import com.diman.sipenguji.model.DataItem
import com.diman.sipenguji.model.Gedung
import com.diman.sipenguji.model.GedungDetail
import com.diman.sipenguji.network.ApiConfig
import com.diman.sipenguji.network.UploadRequestBody
import com.diman.sipenguji.util.getFileName
import com.diman.sipenguji.util.snackbar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_scan.*
import kotlinx.android.synthetic.main.dialog_delete_ruangan.*
import kotlinx.android.synthetic.main.dialog_delete_ruangan.btn_close_dialog_deleteRuangan
import kotlinx.android.synthetic.main.dialog_failed.*
import kotlinx.android.synthetic.main.dialog_ruangan_deleted.*
import kotlinx.android.synthetic.main.dialog_upload_image.*
import kotlinx.android.synthetic.main.fragment_add_data.*
import kotlinx.android.synthetic.main.fragment_edit_gedung.*
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

class EditGedungFragment (private val idGedung: Int) : BottomSheetDialogFragment(), UploadRequestBody.UploadCallback {

    private var selectedImage: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_gedung, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        displayData()
        viewListener()
    }

    private fun viewListener() {
        uploadImageHandler()
        btn_update_gedung.setOnClickListener {
            updateDataGedung()
        }
    }

    private fun updateDataGedung() {
            val namaGedung = et_nama_gedung_edit.text.toString().trim()
            val alamatGedung = et_alamat_gedung_edit.text.toString().trim()
            if (selectedImage == null) {
                //image not selected yet
                edit_gedung_root.snackbar("Mohon pilih gambar gedung")
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

            val client = ApiConfig.getApiService().updateGedung(
                idGedung.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                namaGedung.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                alamatGedung.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                MultipartBody.Part.createFormData("img", file.name, body),
                "Latitude".toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "Longitude".toRequestBody("multipart/form-data".toMediaTypeOrNull())
            )

            client.enqueue(object : Callback<Gedung> {
                override fun onResponse(call: Call<Gedung>, response: Response<Gedung>) {
                    if (response.isSuccessful){
                        showSuccessDialog(activity!!)
                    } else {
                        showFailedDialog(activity!!)
                    }
                }

                override fun onFailure(call: Call<Gedung>, t: Throwable) {
                    showFailedDialog(activity!!)
                }

            })
    }

    private fun showSuccessDialog(context: Context){
        val builder = AlertDialog.Builder(context)
        builder.setView(R.layout.dialog_ruangan_deleted)

        val dialog: AlertDialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.show()

        dialog.tv_message_dialog.text = "Data berhasil diupdate"

        dialog.btn_close_dialog_deletedRuangan.setOnClickListener {
            dialog.dismiss()
        }

        dialog.btn_dialog_ok.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun showFailedDialog(context: Context){
        val builder = AlertDialog.Builder(context)
        builder.setView(R.layout.dialog_failed)

        val dialog: AlertDialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.show()

        dialog.btn_close_dialog_failed.setOnClickListener {
            dialog.dismiss()
        }

        dialog.btn_dialog_tutup.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun uploadImageHandler() {
        var latestTmpUri: Uri? = null

        val uploadFromGallery = registerForActivityResult(ActivityResultContracts.GetContent()) {
            iv_upload_pict_gedung_edit.setImageURI(it)
            cv_placeholderHint_gedung_edit.visibility = View.GONE
            //assign uri ke variable selectedImage
            selectedImage = it
        }

        val uploadFromCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                latestTmpUri?.let { uri ->
                    iv_upload_pict_gedung_edit.setImageURI(uri)
                    cv_placeholderHint_gedung_edit.visibility = View.GONE
                    //assign uri ke variable selectedImage
                    selectedImage = uri
                }
            }
        }

        //show dialog saat pilih gambar yang mau di upload
        iv_upload_pict_gedung_edit.setOnClickListener {
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

    private fun displayData() {
        val client = ApiConfig.getApiService().getGedung(idGedung)
        client.enqueue(object : Callback<GedungDetail> {
            override fun onResponse(call: Call<GedungDetail>, response: Response<GedungDetail>) {
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    et_nama_gedung_edit.text =
                        Editable.Factory.getInstance().newEditable(data?.namaGedung)
                    et_alamat_gedung_edit.text =
                        Editable.Factory.getInstance().newEditable(data?.alamat)
                    Glide.with(requireActivity())
                        .load("https://images.unsplash.com/photo-1523050854058-8df90110c9f1?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80")
                        .apply(
                            RequestOptions().centerCrop().placeholder(R.drawable.banner_img)
                        )
                        .into(iv_upload_pict_gedung_edit)
                } else {
                    ll_form_gedung_edit_root.snackbar(response.message())
                }
            }

            override fun onFailure(call: Call<GedungDetail>, t: Throwable) {
                ll_form_gedung_edit_root.snackbar(t.printStackTrace().toString())
            }

        })
    }

    override fun onProgressUpdate(percentage: Int) {
        //update progressbar upload image
    }
}