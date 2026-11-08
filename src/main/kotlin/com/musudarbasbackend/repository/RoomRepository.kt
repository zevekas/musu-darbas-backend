package com.musudarbasbackend.repository

import com.musudarbasbackend.entity.Room
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface RoomRepository : JpaRepository<Room, UUID> {
    fun findByDormIdAndRoomNumber(
        dormId: UUID,
        roomNumber: String,
    ): Room?
}
