package com.vairuoklaisvai.musudarbasbackend.service

import com.vairuoklaisvai.musudarbasbackend.model.Dorm
import com.vairuoklaisvai.musudarbasbackend.model.FetchDormsResponse
import com.vairuoklaisvai.musudarbasbackend.repository.DormRepository
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
