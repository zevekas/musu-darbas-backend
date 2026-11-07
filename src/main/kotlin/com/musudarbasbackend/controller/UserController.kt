package com.musudarbasbackend.controller

import com.musudarbasbackend.model.UserDataRequest
import com.musudarbasbackend.model.UserResponse
import com.musudarbasbackend.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/app/user")
@RestController
class UserController(
    private val service: UserService,
) {
    @GetMapping
    fun getUserData(): UserResponse = service.getUserData()

    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun saveUserData(
        @RequestBody userData: UserDataRequest,
    ) {
        service.saveUserData(userData)
    }
}
