package com.diman.sipenguji.network

import com.diman.sipenguji.model.Banner
import com.diman.sipenguji.model.Calculate
import com.diman.sipenguji.model.Gedung
import com.diman.sipenguji.model.Ruangan
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    // get list gedung
    @GET("api/gedung")
    fun getListGedung(): Call<Gedung>

    // get data gedung by Id
    @GET("api/gedung/{id}")
    fun getGedung(@Query("id")id: Int): Call<Gedung>

    // post data gedung using field x-www-form-urlencoded
    @FormUrlEncoded
    @POST("api/gedung")
    fun createGedung(
        @Field("nama_gedung") nama: String,
        @Field("alamat") alamat: String,
        @Field("jumlah_ruangan") jml_ruangan: Int,
        @Field("latitude") lat: Float,
        @Field("longitude") lng: Float
    ): Call<Gedung>

    //getList Ruangan
    @GET("api/ruangan")
    fun getListRuangan() : Call<Ruangan>

    //get Ruangan By Id
    @GET("api/ruangan")
    fun getRuangan(@Query("id")id: Int) : Call<Ruangan>

    @GET("api/ruangan")
    fun getListRuanganByGedungId(@Query("gedung_id")gedung_id: Int) : Call<Ruangan>

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
}