package com.musudarbasbackend.controller

import com.musudarbasbackend.model.Dorm
import com.musudarbasbackend.model.FetchDormsResponse
import com.musudarbasbackend.model.UserResponse
import com.musudarbasbackend.service.DormService
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.util.UUID

@WebMvcTest(DormController::class)
@AutoConfigureMockMvc(addFilters = false)
@Import(DormControllerTest.MockConfig::class)
class DormControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var service: DormService

    @BeforeEach
    fun resetMocks() {
        clearMocks(service)
    }

    @Test
    fun `returns dorm list`() {
        // given
        val dormId = UUID.randomUUID()
        every { service.fetchAllDorms() } returns
            FetchDormsResponse(
                listOf(
                    Dorm(id = dormId, address = "Baltupiai"),
                ),
            )

        // when & then
        mockMvc
            .get("/api/app/dorm")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.dorms[0].id") { value(dormId.toString()) }
                jsonPath("$.dorms[0].address") { value("Baltupiai") }
            }

        verify(exactly = 1) { service.fetchAllDorms() }
    }

    @TestConfiguration
    class MockConfig {
        @Bean
        fun dormService(): DormService = mockk()
    }
}
