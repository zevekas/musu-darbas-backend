package com.musudarbasbackend.service

import com.musudarbasbackend.config.CurrentUser
import com.musudarbasbackend.entity.Dorm
import com.musudarbasbackend.entity.Room
import com.musudarbasbackend.entity.User
import com.musudarbasbackend.model.UserDataRequest
import com.musudarbasbackend.repository.DormRepository
import com.musudarbasbackend.repository.UserRepository
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.UUID

class UserServiceTest {
    private val userRepository = mockk<UserRepository>()
    private val currentUser = mockk<CurrentUser>()
    private val dormRepository = mockk<DormRepository>()
    private val roomService = mockk<RoomService>()
    private val service = UserService(userRepository, currentUser, dormRepository, roomService)

    private fun newUser() = User("clerk_1", email = "old@mail.com", balance = BigDecimal.ZERO)

    @Test
    fun `createUser saves user with clerk id, email and zero balance`() {
        val saved = slot<User>()
        every { userRepository.save(capture(saved)) } answers { saved.captured }

        val result = service.createUser("clerk_1", "a@mail.com")

        assertEquals("a@mail.com", saved.captured.email)
        assertEquals(0, BigDecimal.ZERO.compareTo(saved.captured.balance))
        assertSame(saved.captured, result)
    }

    @Test
    fun `updateEmail changes email and saves`() {
        val user = newUser()
        every { userRepository.save(user) } returns user

        val result = service.updateEmail(user, "new@mail.com")

        assertEquals("new@mail.com", result.email)
        verify(exactly = 1) { userRepository.save(user) }
    }

    @Test
    fun `getUserData returns username, dorm address and room number`() {
        val user = newUser().apply { username = "vytas" }
        val dorm = mockk<Dorm> { every { address } returns "Baltupiai" }
        user.room =
            mockk<Room> {
                every { this@mockk.dorm } returns dorm
                every { roomNumber } returns "a101"
            }
        every { currentUser.user } returns user

        val result = service.getUserData()

        assertEquals("vytas", result.username)
        assertEquals("Baltupiai", result.dorm)
        assertEquals("a101", result.room)
    }

    @Test
    fun `getUserData returns null dorm and room when user has no room`() {
        val user = newUser().apply { username = "vytas" }
        every { currentUser.user } returns user

        val result = service.getUserData()

        assertEquals("vytas", result.username)
        assertNull(result.dorm)
        assertNull(result.room)
    }

    @Test
    fun `saveUserData normalizes room number and assigns room`() {
        val dormId = UUID.randomUUID()
        val user = newUser()
        val newRoom = mockk<Room> { every { id } returns UUID.randomUUID() }
        every { currentUser.user } returns user
        every { roomService.findOrCreateRoom(dormId, "a101") } returns newRoom
        every { userRepository.saveAndFlush(user) } returns user

        service.saveUserData(UserDataRequest(username = "vytas", dorm = dormId, room = " A 101 "))

        assertEquals("vytas", user.username)
        assertSame(newRoom, user.room)
        verify(exactly = 1) { roomService.findOrCreateRoom(dormId, "a101") }
        verify(exactly = 1) { userRepository.saveAndFlush(user) }
        verify(exactly = 0) { roomService.deleteRoomIfEmpty(any()) }
    }

    @Test
    fun `saveUserData deletes old room if empty when user moves`() {
        val dormId = UUID.randomUUID()
        val oldRoomId = UUID.randomUUID()
        val user = newUser().apply { room = mockk<Room> { every { id } returns oldRoomId } }
        val newRoom = mockk<Room> { every { id } returns UUID.randomUUID() }
        every { currentUser.user } returns user
        every { roomService.findOrCreateRoom(dormId, "b202") } returns newRoom
        every { userRepository.saveAndFlush(user) } returns user
        every { roomService.deleteRoomIfEmpty(oldRoomId) } just Runs

        service.saveUserData(UserDataRequest(username = "vytas", dorm = dormId, room = "B 202"))

        verifyOrder {
            userRepository.saveAndFlush(user)
            roomService.deleteRoomIfEmpty(oldRoomId)
        }
    }

    @Test
    fun `saveUserData does not delete room when user stays in the same room`() {
        val dormId = UUID.randomUUID()
        val roomId = UUID.randomUUID()
        val room = mockk<Room> { every { id } returns roomId }
        val user = newUser().apply { this.room = room }
        every { currentUser.user } returns user
        every { roomService.findOrCreateRoom(dormId, "a101") } returns room
        every { userRepository.saveAndFlush(user) } returns user

        service.saveUserData(UserDataRequest(username = "vytas", dorm = dormId, room = "a101"))

        verify(exactly = 0) { roomService.deleteRoomIfEmpty(any()) }
    }
}
