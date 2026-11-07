package com.vairuoklaisvai.musudarbasbackend.repository

import com.vairuoklaisvai.musudarbasbackend.entity.Dorm
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface DormRepository : JpaRepository<Dorm, UUID>
