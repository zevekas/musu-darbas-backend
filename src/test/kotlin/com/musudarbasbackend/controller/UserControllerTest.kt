package com.musudarbasbackend.controller

import com.musudarbasbackend.model.UserDataRequest
import com.musudarbasbackend.model.UserResponse
import com.musudarbasbackend.service.UserService
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
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
import org.springframework.test.web.servlet.patch
import java.util.UUID

@WebMvcTest(UserController::class)
@AutoConfigureMockMvc(addFilters = false)
@Import(UserControllerTest.MockConfig::class)
class UserControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var service: UserService

    @BeforeEach
    fun resetMocks() {
        clearMocks(service)
    }

    @Test
    fun `returns user data for the current user`() {
        // given
        val userId = UUID.randomUUID()
        every { service.getUserData() } returns
            UserResponse(
                username = "Petriukas",
                dorm = "Baltupiai",
                room = "23a",
            )

        // when & then
        mockMvc
            .get("/api/app/user")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.username") { value("Petriukas") }
                jsonPath("$.dorm") { value("Baltupiai") }
                jsonPath("$.room") { value("23a") }
            }

        verify(exactly = 1) { service.getUserData() }
    }

    @Test
    fun `saves user data for the current user`() {
        // given
        val dormId = UUID.randomUUID()
        val request =
            UserDataRequest(
                username = "Petriukas",
                dorm = dormId,
                room = "23a",
            )
        every { service.saveUserData(request) } just runs

        // when & then
        mockMvc
            .patch("/api/app/user") {
                contentType = MediaType.APPLICATION_JSON
                content =
                    """
                    {
                        "username": "Petriukas",
                        "dorm": "$dormId",
                        "room": "23a"
                    }
                    """.trimIndent()
            }.andExpect { status { isNoContent() } }

        verify(exactly = 1) { service.saveUserData(request) }
    }

    @TestConfiguration
    class MockConfig {
        @Bean
        fun userService(): UserService = mockk()
    }
}
