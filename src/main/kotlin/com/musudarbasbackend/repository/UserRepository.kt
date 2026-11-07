package com.musudarbasbackend.repository

import com.musudarbasbackend.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository : JpaRepository<User, UUID> {
    fun findByClerkId(clerkId: String): User?
}
