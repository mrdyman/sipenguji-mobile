package com.diman.sipenguji

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.diman.sipenguji.model.Auth
import com.diman.sipenguji.model.User
import com.diman.sipenguji.network.ApiConfig
import com.diman.sipenguji.util.SharedPreferences
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.help_dialog.view.*
import kotlinx.android.synthetic.main.login_dialog_400.view.*
import kotlinx.android.synthetic.main.login_dialog_404.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewListener()
    }

    private fun viewListener() {
        val sharePref = SharedPreferences(this)
        val isLogin = sharePref.isLogin
        val role = sharePref.userRole
        if (isLogin){
            if (role == 1){
                openMahasiswaPage()
            } else {
                openAdminPage()
            }
        }
        login()
        help()
    }

    private fun login() {
        btn_login.setOnClickListener {
            val username = et_username.text.toString().trim()
            val password = et_password.text.toString().trim()
            if (username.isBlank()) {
                Toast.makeText(this, "username tidak boleh kosong", Toast.LENGTH_LONG).show()
            } else if (password.isEmpty()) {
                Toast.makeText(this, "password tidak boleh kosong", Toast.LENGTH_LONG).show()
            } else {
                val client = ApiConfig.getApiService().login(username, password)
                client.enqueue(object : Callback<User>{
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful) {
                            val data = response.body()
                            Log.d("Response", "$data")
                            val message = data?.message
                            val status = data?.status
                            val role = data?.data?.role
                            val signature = data?.data?.signature
                            if (status == true) {
                                if (role.toString().toInt() == 0) {
                                    //user = admin
                                    openAdminPage()
                                } else if (role.toString().toInt() == 1) {
                                    //user = mahasiswa
                                    openMahasiswaPage()
                                } else {
                                    //user tidak diketahui
                                    Log.d("Response", "Failed to login user not found : $message")
                                }

                                //simpan data user ke share preference
                                val sharePref = SharedPreferences(this@LoginActivity)
                                sharePref.isLogin = true
                                sharePref.userSignature = signature
                                sharePref.userRole = role!!.toInt()

                            } else {
                                Log.d("Response", "Failed to login with message : $message")
                            }
                        } else {
                            Log.d("Response", "Failed with message : ${response.message()}")
                            //cek kode response API
                            when(response.code()){
                                404 -> {
                                    showMessageDialog(404)
                                }
                                400 -> {
                                    showMessageDialog(400)
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Log.d("Response", "Failed connect to API Auth with message : ${t.printStackTrace()}")
                    }

                })
            }
        }
    }

    private fun help() {
        tv_bantuan.setOnClickListener {
            showMessageDialog(0)
        }
    }

    private fun showMessageDialog(code : Int){
        when(code) {
            0 -> {
                //show help dialog
                val mDialogView = LayoutInflater.from(this).inflate(R.layout.help_dialog, null)
                val mBuilder = AlertDialog.Builder(this)
                    .setView(mDialogView)
                    .setCancelable(false)
                val  mAlertDialog = mBuilder.show()
                mAlertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mDialogView.btn_close_dialog_help.setOnClickListener {
                    mAlertDialog.dismiss()
                }
            }
            404 -> {
                //user not found
                val mDialogView = LayoutInflater.from(this).inflate(R.layout.login_dialog_404, null)
                val mBuilder = AlertDialog.Builder(this)
                    .setView(mDialogView)
                    .setCancelable(false)
                val  mAlertDialog = mBuilder.show()
                mAlertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mDialogView.btn_close_dialog.setOnClickListener {
                    mAlertDialog.dismiss()
                }
            }
            400 -> {
                //wrong password
                val mDialogView = LayoutInflater.from(this).inflate(R.layout.login_dialog_400, null)
                val mBuilder = AlertDialog.Builder(this)
                    .setView(mDialogView)
                    .setCancelable(false)
                val  mAlertDialog = mBuilder.show()
                mAlertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                mDialogView.btn_close_dialog_400.setOnClickListener {
                    mAlertDialog.dismiss()
                }
            }
        }
    }

    private fun openAdminPage(){
        //buka mainActivity (halaman admin)
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        this.finish()
    }

    private fun openMahasiswaPage(){
        //buka scanActivity(halaman mahasiswa)
        val i = Intent(this, ScanActivity::class.java)
        startActivity(i)
        this.finish()
    }
}