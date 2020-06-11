package io.web.covid19tracker.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.web.covid19tracker.config.ApiAppConfig
import io.web.covid19tracker.models.Country
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CountryServiceTest {

    @MockK
    private lateinit var apiAppConfig: ApiAppConfig

    @InjectMockKs
    private lateinit var countryService: CountryService

    private lateinit var mockWebServer: MockWebServer

    @BeforeEach
    internal fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        val url = mockWebServer.url("/")
        apiAppConfig = ApiAppConfig(url.toString(), "/api")
        countryService = CountryService(apiAppConfig)
        MockKAnnotations.init(this)
    }


    @AfterEach
    internal fun tearDown() {
        mockWebServer.shutdown()
    }


    @Test
    fun shouldGetCountryNames() {
        mockWebServer.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(countryResponse().trimIndent()))

        val countries = countryService.getCountries()
        val countryResponseList = jacksonObjectMapper()
                .readValue(countryResponse(), object : TypeReference<List<Country>>() {})

        assertEquals(2, countries?.size)
        assertEquals(countryResponseList[0], countries?.get(0))
        assertEquals(countryResponseList[1], countries?.get(1))
    }

    @Test
    fun shouldReturnCountryByCountryName() {
        val countryList = jacksonObjectMapper()
                .readValue(countryResponse(), object : TypeReference<List<Country>>() {})
        val country = countryService.getCountry(countryList, countryList[0].country)

        assertEquals(countryList[0], country)
    }

    private fun countryResponse(): String {
        return """[
                        {
                            "Country": "Country name",
                            "Slug" : "Country Slug",
                            "ISO2" : "Country ISO2"
                        },
                        {
                            "Country": "Country name 2",
                            "Slug" : "Country Slug 2",
                            "ISO2" : "Country ISO2 2"
                        }
                    ]"""
    }
}