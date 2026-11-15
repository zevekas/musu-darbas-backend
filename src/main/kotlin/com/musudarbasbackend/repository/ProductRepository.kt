package com.musudarbasbackend.repository

import com.musudarbasbackend.entity.Product
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ProductRepository : JpaRepository<Product, UUID>