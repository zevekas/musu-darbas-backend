package com.vairuoklaisvai.musudarbasbackend.service

import com.vairuoklaisvai.backend.exception.BadRequestException
import com.vairuoklaisvai.musudarbasbackend.entity.Room
import com.vairuoklaisvai.musudarbasbackend.repository.DormRepository
import com.vairuoklaisvai.musudarbasbackend.repository.RoomRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class RoomService(
    private val repository: RoomRepository,
    private val dormRepository: DormRepository,
) {
    private val logger = KotlinLogging.logger { }

    @Transactional
    fun findOrCreateRoom(
        dormId: UUID,
        roomNumber: String,
    ): Room {
        val dorm = dormRepository.findByIdOrNull(dormId) ?: throw BadRequestException("Dorm ID $dormId not found")
        val room = repository.findByDormIdAndRoomNumber(dormId, roomNumber)

        if (room == null) {
            logger.error { "Room not found for dormId=$dormId" }

            val newRoom =
                repository.save(
                    Room(
                        dorm = dorm,
                        roomNumber = roomNumber,
                    ),
                )

            logger.info { "Created new room for dormId=$dormId" }
            return newRoom
        }

        return room
    }
}
