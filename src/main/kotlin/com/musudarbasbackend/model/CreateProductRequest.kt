package com.musudarbasbackend.model

import java.math.BigDecimal

data class CreateProductRequest(
    val name: String,
    val type: ProductType,
    val status: ProductStatus? = null,
    val amount: Int? = null,
    val price: BigDecimal? = null,
    val imageUrl: String? = null,
    val addToRoom: Boolean = false,
)