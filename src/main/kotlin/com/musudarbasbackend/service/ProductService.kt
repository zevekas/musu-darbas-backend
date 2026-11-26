package com.musudarbasbackend.service

import com.musudarbasbackend.config.CurrentUser
import com.musudarbasbackend.entity.Product
import com.musudarbasbackend.entity.RoomProduct
import com.musudarbasbackend.exception.BadRequestException
import com.musudarbasbackend.model.CreateProductRequest
import com.musudarbasbackend.model.ProductStatus
import com.musudarbasbackend.model.ProductType
import com.musudarbasbackend.repository.ProductRepository
import com.musudarbasbackend.repository.RoomProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val roomProductRepository: RoomProductRepository,
    private val currentUser: CurrentUser,
) {
    @Transactional
    fun createProduct(request: CreateProductRequest) {
        val name = request.name.trim()
        if (name.isEmpty()) throw BadRequestException("Product name must not be blank")

        val product = productRepository.save(
            Product(name = name, type = request.type, imageUrl = request.imageUrl),
        )

        if (request.addToRoom) {
            val room = currentUser.user.room
                ?: throw BadRequestException("User has not joined a room yet")

            val price = request.price
                ?: throw BadRequestException("Price is required when adding to room")
            if (price < BigDecimal.ZERO) throw BadRequestException("Price must not be negative")

            var status: ProductStatus? = null
            var amount: Int? = null

            when (request.type) {
                ProductType.QUANTITY -> {
                    val requestedAmount = request.amount
                        ?: throw BadRequestException("Amount is required for QUANTITY products")
                    if (requestedAmount < 0) throw BadRequestException("Amount must not be negative")
                    amount = requestedAmount
                }
                ProductType.STATUS -> {
                    status = request.status
                        ?: throw BadRequestException("Status is required for STATUS products")
                }
            }

            roomProductRepository.save(
                RoomProduct(
                    product = product,
                    room = room,
                    status = status,
                    amount = amount,
                    price = price,
                ),
            )
        }
    }
}