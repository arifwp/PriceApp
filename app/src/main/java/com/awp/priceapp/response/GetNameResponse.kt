package com.awp.priceapp.response

import com.google.gson.annotations.SerializedName

data class GetNameResponse (

    @field:SerializedName("status")
    val status: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data_product")
    val data_product: List<listName>

)

data class listName (

    @field:SerializedName("ProductName")
    val ProductName: String? = null,

)