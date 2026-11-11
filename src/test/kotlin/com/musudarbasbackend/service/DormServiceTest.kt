package com.musudarbasbackend.service

import com.musudarbasbackend.repository.DormRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID
import com.musudarbasbackend.entity.Dorm as DormEntity

class DormServiceTest {
    private val dormRepository = mockk<DormRepository>()
    private val service = DormService(dormRepository)

    @Test
    fun `fetchAllDorms maps entities to response`() {
        // given
        val id1 = UUID.randomUUID()
        val id2 = UUID.randomUUID()
        every { dormRepository.findAll() } returns
            listOf(
                mockk<DormEntity> {
                    every { id } returns id1
                    every { address } returns "Baltupiai"
                },
                mockk<DormEntity> {
                    every { id } returns id2
                    every { address } returns "Sauletekis"
                },
            )

        // when
        val result = service.fetchAllDorms()

        // then
        assertEquals(2, result.dorms.size)
        assertEquals(id1, result.dorms[0].id)
        assertEquals("Baltupiai", result.dorms[0].address)
        assertEquals(id2, result.dorms[1].id)
        assertEquals("Sauletekis", result.dorms[1].address)
        verify(exactly = 1) { dormRepository.findAll() }
    }

    @Test
    fun `fetchAllDorms returns empty list when there are no dorms`() {
        every { dormRepository.findAll() } returns emptyList()

        val result = service.fetchAllDorms()

        assertTrue(result.dorms.isEmpty())
    }

    @Test
    fun `fetchAllDorms throws when a dorm has no id`() {
        every { dormRepository.findAll() } returns
            listOf(
                mockk<DormEntity> {
                    every { id } returns null
                    every { address } returns "Baltupiai"
                },
            )

        assertThrows<NullPointerException> { service.fetchAllDorms() }
    }
}
