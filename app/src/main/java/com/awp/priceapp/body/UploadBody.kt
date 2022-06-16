package com.awp.priceapp.body

import com.google.gson.annotations.SerializedName
import java.util.*

data class UploadBody(

    @field:SerializedName("ProductName")
    val ProductName: String? = null,

    @field:SerializedName("ProductCategory")
    val ProductCategory: String? = null,

    @field:SerializedName("Price")
    val Price: Int? = null,

    @field:SerializedName("Quantity")
    val Quantity: Int? = null,

    @field:SerializedName("Cost")
    val Cost: Int? = null,

    @field:SerializedName("Description")
    val Description: String? = null,

    @field:SerializedName("Photos")
    val Photos: String? = null,
)