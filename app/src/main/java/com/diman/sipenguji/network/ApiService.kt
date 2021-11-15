package com.diman.sipenguji.network

import com.diman.sipenguji.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    // get list gedung
    @GET("api/gedung")
    fun getListGedung(): Call<Gedung>

    // get data gedung by Id
    @GET("api/gedung")
    fun getGedung(@Query("id") id: Int): Call<GedungDetail>

    //get Last Added Gedung
    @GET("api/gedung")
    fun getLastGedung(@Query("isLimit") limit : Int) : Call<Gedung>

    // post data gedung using field x-www-form-urlencoded
    @Multipart
    @POST("api/gedung")
    fun createGedung(
        @Part("nama") nama: RequestBody,
        @Part("alamat") alamat: RequestBody,
        @Part gambar : MultipartBody.Part,
        @Part("latitude") lat: RequestBody,
        @Part("longitude") lng: RequestBody
    ): Call<Gedung>

    //update data gedung
    @Multipart
    @POST("api/gedung")
    fun updateGedung(
        @Part("id") id: RequestBody,
        @Part("nama") nama: RequestBody,
        @Part("alamat") alamat: RequestBody,
        @Part gambar : MultipartBody.Part,
        @Part("latitude") lat: RequestBody,
        @Part("longitude") lng: RequestBody
    ) : Call<Gedung>

    //delete data gedung
    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "api/gedung/{id}", hasBody = true)
    fun deleteGedung(@Field("id") id: Int) : Call<Gedung>

    //getList Ruangan
    @GET("api/ruangan")
    fun getListRuangan() : Call<Ruangan>

    //get Ruangan By Id
    @GET("api/ruangan")
    fun getRuangan(@Query("id")id: Int) : Call<Ruangan>

    //get Ruangan By Gedung Id
    @GET("api/ruangan")
    fun getListRuanganByGedungId(@Query("gedung_id")gedung_id: Int) : Call<Ruangan>

    //get Ruangan By Name
    @GET("api/ruangan")
    fun getRuanganByName(@Query("nama_ruangan")nama_gedung : String) : Call<Ruangan>

    //get Last Added Ruangan
    @GET("api/ruangan")
    fun getLastRuangan(@Query("isLimit") limit : Int) : Call<Ruangan>

    //get Ruangan Detail by nisn mahasiswa
    @GET("api/ruangan/detail")
    fun getRuanganByNisn(@Query("nisn")nisn : String) : Call<RuanganDetail>

    //create data ruangan
    @FormUrlEncoded
    @POST("api/ruangan")
    fun createRuangan(
        @Field("nama_ruangan") namaRuangan : String,
        @Field("jumlah_peserta") jumlahPeserta : Int,
        @Field("latitude") latitude : String,
        @Field("longitude") longitude : String,
        @Field("id_gedung") idGedung : Int
    ) : Call<Ruangan>

    //update data ruangan
    @PUT("api/ruangan/")
    fun updateRuangan(@Body data : DataRuangan) : Call<Ruangan>

    //delete data ruangan
    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "api/ruangan/{id}", hasBody = true)
    fun deleteRuangan(@Field("id") id: Int) : Call<Ruangan>

    //get Gambar Ruangan by Id Ruangan
    @GET("api/gambar")
    fun getGambar(@Query("id_ruangan")id_ruangan : Int) : Call<GambarRuangan>

    //calculate shortest path
    @FormUrlEncoded
    @POST("api/calculate")
    fun calculate (
        @Field("source") source: String,
        @Field("destination") destination: String?,
        @Field("decoded_Path") decodedPath: String,
    ) : Call<Calculate>

    //Banner

    //getListBanner
    @GET("api/banner")
    fun getListBanner() : Call<Banner>

    //Authentication User

    //login
    @FormUrlEncoded
    @POST("api/user/login")
    fun login(
        @Field("username") username : String,
        @Field("password") password : String,
    ) : Call<User>

    //get data logged user
    @GET("api/user")
    fun getUser(@Query ("signature") signature : String) : Call<User>

    //Peserta Ujian

    //get Data Peserta Ujian by ruangan id
    @GET("api/peserta")
    fun getPeserta(@Query("id_ruangan") id: Int) : Call <Peserta>
}