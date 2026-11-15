package com.musudarbasbackend.model

import java.util.UUID

data class ProductResponse(
    val id: UUID,
    val name: String,
    val type: ProductType,
    val status: ProductStatus,
    val amount: Int,
    val imageUrl: String?,
)