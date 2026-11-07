package com.vairuoklaisvai.musudarbasbackend.repository

import com.vairuoklaisvai.musudarbasbackend.entity.Room
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface RoomRepository : JpaRepository<Room, UUID> {
    fun findByDormIdAndRoomNumber(
        dormId: UUID,
        roomNumber: String,
    ): Room?
}
