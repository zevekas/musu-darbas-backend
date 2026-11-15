package com.musudarbasbackend.model

data class CreateProductRequest(
    val name: String,
    val type: ProductType,
    val status: ProductStatus? = null,
    val amount: Int? = null,
    val imageUrl: String? = null,
)