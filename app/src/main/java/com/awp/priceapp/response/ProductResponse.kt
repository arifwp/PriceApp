package com.awp.priceapp.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*
import kotlin.collections.ArrayList

data class ProductResponse (

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("data")
    val data: List<ListProduct>

)

@Parcelize
data class ListProduct(

    @field:SerializedName("id")
    val id: String? = null,

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

    @field:SerializedName("insertedAt")
    val insertedAt: Date? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: Date? = null

): Parcelable