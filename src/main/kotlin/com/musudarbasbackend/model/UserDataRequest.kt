package com.musudarbasbackend.model

import java.util.UUID

data class UserDataRequest(
    val username: String,
    val dorm: UUID,
    val room: String,
)
