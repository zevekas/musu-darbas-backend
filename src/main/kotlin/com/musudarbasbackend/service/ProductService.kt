package com.musudarbasbackend.service

import com.musudarbasbackend.config.CurrentUser
import com.musudarbasbackend.entity.Product
import com.musudarbasbackend.entity.RoomProduct
import com.musudarbasbackend.exception.BadRequestException
import com.musudarbasbackend.model.CreateProductRequest
import com.musudarbasbackend.model.ProductResponse
import com.musudarbasbackend.model.ProductStatus
import com.musudarbasbackend.model.ProductType
import com.musudarbasbackend.repository.ProductRepository
import com.musudarbasbackend.repository.RoomProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val roomProductRepository: RoomProductRepository,
    private val currentUser: CurrentUser,
) {
    @Transactional
    fun createProduct(request: CreateProductRequest): ProductResponse {
        val name = request.name.trim()
        if (name.isEmpty()) throw BadRequestException("Product name must not be blank")

        val room = currentUser.user.room
            ?: throw BadRequestException("User has not joined a room yet")

        val status: ProductStatus
        val amount: Int
        when (request.type) {
            ProductType.QUANTITY -> {
                amount = request.amount
                    ?: throw BadRequestException("Amount is required for QUANTITY products")
                if (amount < 0) throw BadRequestException("Amount must not be negative")
                status = if (amount > 0) ProductStatus.FULL else ProductStatus.EMPTY
            }
            ProductType.STATUS -> {
                status = request.status
                    ?: throw BadRequestException("Status is required for STATUS products")
                amount = 1
            }
        }

        val product = productRepository.save(
            Product(name = name, type = request.type, imageUrl = request.imageUrl),
        )
        val roomProduct = roomProductRepository.save(
            RoomProduct(product = product, room = room, status = status, amount = amount),
        )

        return ProductResponse(
            id = roomProduct.id!!,
            name = product.name,
            type = product.type,
            status = roomProduct.status,
            amount = roomProduct.amount,
            imageUrl = product.imageUrl,
        )
    }
}
