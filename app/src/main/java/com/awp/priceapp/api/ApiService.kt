package com.awp.priceapp.api

import com.awp.priceapp.body.UploadBody
import com.awp.priceapp.response.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("products")
    fun getAllPost(): retrofit2.Call<ProductResponse>

    @POST("products/post")
    fun uploadImage(
        @Body info: UploadBody
    ): retrofit2.Call<FileUploadResponse>

    @DELETE("products/{id}/delete")
    fun deleteData(
        @Path("id") id: String
    ): Call<Void>

    @GET("formula")
    fun searchName(): retrofit2.Call<GetNameResponse>

}