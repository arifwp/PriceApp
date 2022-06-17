package com.awp.priceapp.body

data class DataResponse(
	val productNo: String? = null,
	val elasticityCoef: Double? = null,
	val maxPrice: Double? = null,
	val productName: String? = null,
	val interceptCoef: Double? = null,
	val minPrice: Double? = null
)

