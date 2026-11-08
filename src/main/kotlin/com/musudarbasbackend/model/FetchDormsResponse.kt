package com.musudarbasbackend.model

import java.util.UUID

data class FetchDormsResponse(
    val dorms: List<Dorm>,
)

data class Dorm(
    val id: UUID,
    val address: String,
)
