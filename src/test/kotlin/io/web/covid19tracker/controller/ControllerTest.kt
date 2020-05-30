package io.web.covid19tracker.controller

import io.mockk.every
import io.mockk.mockk
import io.web.covid19tracker.models.Data
import io.web.covid19tracker.service.DataService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import reactor.core.publisher.Mono

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
internal class ControllerTest {

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext
    private lateinit var mockMvc: MockMvc

    private val dataService = mockk<DataService>()

    @BeforeEach
    internal fun setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
    }

    @Disabled
    @Test
    internal fun `should show data`() {
        val data = listOf(Data("province", "country", 200, 190))
        every { dataService.fetchData() } returns Mono.just(data)

        this.mockMvc.perform(MockMvcRequestBuilders.get("/")).andExpect {
            it.response.status == HttpStatus.OK.value()
        }.andExpect(model().attributeExists("data"))
    }
}