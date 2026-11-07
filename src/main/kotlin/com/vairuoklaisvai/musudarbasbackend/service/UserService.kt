package com.vairuoklaisvai.musudarbasbackend.service

import com.vairuoklaisvai.musudarbasbackend.config.CurrentUser
import com.vairuoklaisvai.musudarbasbackend.entity.User
import com.vairuoklaisvai.musudarbasbackend.model.UserDataRequest
import com.vairuoklaisvai.musudarbasbackend.model.UserResponse
import com.vairuoklaisvai.musudarbasbackend.repository.DormRepository
import com.vairuoklaisvai.musudarbasbackend.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class UserService(
    private val userRepository: UserRepository,
    private val currentUser: CurrentUser,
    private val dormRepository: DormRepository,
    private val roomService: RoomService,
) {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun createUser(
        clerkId: String,
        email: String,
    ): User = userRepository.save(User(clerkId, email = email, balance = BigDecimal(0.0)))

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun updateEmail(
        user: User,
        newEmail: String,
    ): User {
        user.email = newEmail
        return userRepository.save(user)
    }

    fun getUserData(): UserResponse {
        val user = currentUser.user

        return UserResponse(
            username = user.username,
            dorm = user.room?.dorm?.address,
            room = user.room?.roomNumber,
        )
    }

    @Transactional
    fun saveUserData(request: UserDataRequest) {
        val user = currentUser.user

        val roomNumber = request.room.replace(" ", "").lowercase()
        val room = roomService.findOrCreateRoom(request.dorm, roomNumber)

        user.username = request.username
        user.room = room
    }
}
