package io.web.covid19tracker.models

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DataTest {
    @Test
    fun `should return difference in count`() {
        val currentCount = 10
        val previousCount = 8
        val data = Data("province", "country", currentCount, previousCount)
        val difference = data.getDifference()
        assertEquals(difference, currentCount - previousCount)
    }
}