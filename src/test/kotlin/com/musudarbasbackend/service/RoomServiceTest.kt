package com.musudarbasbackend.service

import com.musudarbasbackend.entity.Dorm
import com.musudarbasbackend.entity.Room
import com.musudarbasbackend.exception.BadRequestException
import com.musudarbasbackend.repository.DormRepository
import com.musudarbasbackend.repository.RoomProductRepository
import com.musudarbasbackend.repository.RoomRepository
import com.musudarbasbackend.repository.UserRepository
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.repository.findByIdOrNull
import java.util.UUID

class RoomServiceTest {
    private val repository = mockk<RoomRepository>()
    private val dormRepository = mockk<DormRepository>()
    private val userRepository = mockk<UserRepository>()
    private val roomProductRepository = mockk<RoomProductRepository>()
    private val service = RoomService(repository, dormRepository, userRepository, roomProductRepository)

    private val dormId = UUID.randomUUID()
    private val dorm = mockk<Dorm>(relaxed = true)

    @Test
    fun `findOrCreateRoom returns existing room without saving`() {
        val existing = mockk<Room>()
        every { dormRepository.findByIdOrNull(dormId) } returns dorm
        every { repository.findByDormIdAndRoomNumber(dormId, "a101") } returns existing

        val result = service.findOrCreateRoom(dormId, "a101")

        assertSame(existing, result)
        verify(exactly = 0) { repository.save(any()) }
    }

    @Test
    fun `findOrCreateRoom creates room when none exists`() {
        val saved = slot<Room>()
        every { dormRepository.findByIdOrNull(dormId) } returns dorm
        every { repository.findByDormIdAndRoomNumber(dormId, "a101") } returns null
        every { repository.save(capture(saved)) } answers { saved.captured }

        val result = service.findOrCreateRoom(dormId, "a101")

        assertEquals("a101", saved.captured.roomNumber)
        assertSame(dorm, saved.captured.dorm)
        assertSame(saved.captured, result)
        verify(exactly = 1) { repository.save(any()) }
    }

    @Test
    fun `findOrCreateRoom throws BadRequestException when dorm does not exist`() {
        every { dormRepository.findByIdOrNull(dormId) } returns null

        val ex = assertThrows<BadRequestException> { service.findOrCreateRoom(dormId, "a101") }

        assertEquals("Dorm ID $dormId not found", ex.message)
        verify(exactly = 0) { repository.findByDormIdAndRoomNumber(any(), any()) }
        verify(exactly = 0) { repository.save(any()) }
    }

    @Test
    fun `deleteRoomIfEmpty does nothing when room still has users`() {
        val roomId = UUID.randomUUID()
        every { userRepository.countByRoomId(roomId) } returns 2

        service.deleteRoomIfEmpty(roomId)

        verify(exactly = 0) { roomProductRepository.deleteAllByRoomId(any()) }
        verify(exactly = 0) { repository.deleteById(any()) }
    }

    @Test
    fun `deleteRoomIfEmpty deletes products then room when empty`() {
        val roomId = UUID.randomUUID()
        every { userRepository.countByRoomId(roomId) } returns 0
        every { roomProductRepository.deleteAllByRoomId(roomId) } just Runs
        every { repository.deleteById(roomId) } just Runs

        service.deleteRoomIfEmpty(roomId)

        verifyOrder {
            roomProductRepository.deleteAllByRoomId(roomId)
            repository.deleteById(roomId)
        }
    }
}
