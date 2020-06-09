package com.karan.qrcodescanner

import retrofit2.Call
import retrofit2.http.*

interface UploadData {

    @GET("")
    fun uploadData(@Query("data") data: String): Call<String>
}
