package com.vairuoklaisvai.musudarbasbackend.entity

import com.vairuoklaisvai.musudarbasbackend.model.ProductType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "product")
class Product(
    @Id
    @GeneratedValue
    var id: UUID? = null,
    @Column(name = "name", nullable = false)
    var name: String,
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    var type: ProductType,
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant? = null,
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    var createdAt: Instant? = null,
)
