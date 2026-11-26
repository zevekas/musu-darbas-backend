package com.musudarbasbackend.entity

import com.musudarbasbackend.model.ProductStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "room_product")
class RoomProduct(
    @Id
    @GeneratedValue
    var id: UUID? = null,
    @ManyToOne
    @JoinColumn(name = "product_id")
    var product: Product,
    @ManyToOne
    @JoinColumn(name = "room_id")
    var room: Room,
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = true)
    var status: ProductStatus? = null,
    @Column(name = "amount", nullable = true)
    var amount: Int? = null,
    @Column(name = "price", nullable = false)
    var price: BigDecimal,
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant? = null,
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    var createdAt: Instant? = null,
)