package com.awp.priceapp.api

import com.awp.priceapp.body.UploadBody
import com.awp.priceapp.response.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("products")
    fun getAllPost(): retrofit2.Call<ProductResponse>

    @POST("products/post")
    fun uploadImage(
        @Body info: UploadBody
    ): retrofit2.Call<FileUploadResponse>

    @GET("products/{id}")
    fun getProductById(
        @Path(value = "id", encoded = false) key: String
    ): Call<GetProductResponse>

    @DELETE("products/{id}/delete")
    fun deleteData(
        @Path("id") id: String
    ): Call<Void>

}