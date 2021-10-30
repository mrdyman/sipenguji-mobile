package com.diman.sipenguji.model

import com.google.gson.annotations.SerializedName

data class Auth(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null,

    @field:SerializedName("role")
	val role: String? = null
)
