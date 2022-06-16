package com.awp.priceapp.response

import com.google.gson.annotations.SerializedName

data class FileUploadResponse (

    @field:SerializedName("status")
    val status: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null

)