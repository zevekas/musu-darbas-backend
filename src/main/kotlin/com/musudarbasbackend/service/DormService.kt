package com.musudarbasbackend.service

import com.musudarbasbackend.model.Dorm
import com.musudarbasbackend.model.FetchDormsResponse
import com.musudarbasbackend.repository.DormRepository
import org.springframework.stereotype.Service

@Service
class DormService(
    private val dormRepository: DormRepository,
) {
    fun fetchAllDorms(): FetchDormsResponse {
        val dorms = dormRepository.findAll()

        return FetchDormsResponse(
            dorms.map { dorm ->
                Dorm(
                    dorm.id!!,
                    dorm.address,
                )
            },
        )
    }
}
