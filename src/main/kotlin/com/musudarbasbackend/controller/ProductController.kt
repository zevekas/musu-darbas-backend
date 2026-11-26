package com.musudarbasbackend.controller

import com.musudarbasbackend.model.CreateProductRequest
import com.musudarbasbackend.service.ProductService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/app/product")
class ProductController(
    private val service: ProductService,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createProduct(
        @RequestBody request: CreateProductRequest,
    ) {
        service.createProduct(request)
    }
}