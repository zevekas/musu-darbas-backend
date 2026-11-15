package com.musudarbasbackend.repository

import com.musudarbasbackend.entity.RoomProduct
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface RoomProductRepository : JpaRepository<RoomProduct, UUID> {
    fun findAllByRoomId(roomId: UUID): List<RoomProduct>
}