package com.musudarbasbackend.repository

import com.musudarbasbackend.entity.Dorm
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface DormRepository : JpaRepository<Dorm, UUID>
