package com.musudarbasbackend.repository

import com.musudarbasbackend.entity.RoomProduct
import com.musudarbasbackend.model.ProductStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface RoomProductRepository : JpaRepository<RoomProduct, UUID> {
    fun findAllByRoomId(roomId: UUID): List<RoomProduct>

    fun findAllByRoomIdAndStatus(
        roomId: UUID,
        status: ProductStatus,
    ): List<RoomProduct>

    fun deleteAllByRoomId(roomId: UUID)
}