package com.vairuoklaisvai.musudarbasbackend.controller

import com.vairuoklaisvai.musudarbasbackend.model.FetchDormsResponse
import com.vairuoklaisvai.musudarbasbackend.service.DormService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/app/dorm")
class DormController(
    private val service: DormService,
) {
    @GetMapping
    fun getAllDorms(): FetchDormsResponse = service.fetchAllDorms()
}
