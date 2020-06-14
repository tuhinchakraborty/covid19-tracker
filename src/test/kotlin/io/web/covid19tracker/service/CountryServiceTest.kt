package io.web.covid19tracker.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import io.web.covid19tracker.config.ApiAppConfig
import io.web.covid19tracker.models.Country
import io.web.covid19tracker.models.CountryData
import io.web.covid19tracker.util.getResponseFor
import io.web.covid19tracker.util.getResponseForDates
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CountryServiceTest {

    @MockK
    private lateinit var apiAppConfig: ApiAppConfig

    @InjectMockKs
    private lateinit var countryService: CountryService

    @BeforeEach
    internal fun setUp() {
        apiAppConfig = ApiAppConfig()
        countryService = CountryService(apiAppConfig)
        MockKAnnotations.init(this)
    }

    @Test
    fun shouldReturnListOfCountries() {
        mockkStatic("io.web.covid19tracker.util.UtilKt")
        every { getResponseFor(any(), any()) } returns countryResponse()

        val countries = countryService.getCountries()
        val expectedCountryResponse = jacksonObjectMapper().readValue<List<Country>>(
                countryResponse().string()
        )

        assertEquals(2, countries?.size)
        assertEquals(expectedCountryResponse.first(), countries?.first())
        assertEquals(expectedCountryResponse.last(), countries?.last())
    }

    @Test
    fun shouldReturnCountryByCountryName() {
        val countryList = jacksonObjectMapper()
                .readValue<List<Country>>(countryResponse().string())
        val country = countryService.getCountry(countryList, countryList[0].country)

        assertEquals(countryList[0], country)
    }

    @Test
    fun shouldReturnTotalCountryData() {
        mockkStatic("io.web.covid19tracker.util.UtilKt")
        every { getResponseFor(any(), any()) } returns totalCountryDataResponse()

        val totalCountryData = countryService.getTotalCountryData("sample")
        val expectedTotalCountryData = jacksonObjectMapper().readValue<List<CountryData>>(
                totalCountryDataResponse().string()
        )

        assertEquals(2, totalCountryData.size)
        assertEquals(expectedTotalCountryData.first(), totalCountryData.first())
        assertEquals(expectedTotalCountryData.last(), totalCountryData.last())
    }

    @Test
    fun shouldReturnCurrentCountryData() {
        mockkStatic("io.web.covid19tracker.util.UtilKt")
        every { getResponseForDates(any(), any(), any(), any()) } returns totalCurrentCountryDataResponse()

        val currentCountryData = countryService.getCurrentCountryData("sample")
        val expectedCountryData = jacksonObjectMapper().readValue<List<CountryData>>(
                totalCurrentCountryDataResponse().string()
        )

        assertEquals(expectedCountryData.first(), currentCountryData)
    }

    private fun totalCurrentCountryDataResponse(): ResponseBody {
        return """
            [
                {
                    "Country": "Sample Country",
                    "CountryCode": "",
                    "Province": "",
                    "City": "",
                    "CityCode": "",
                    "Lat": "0",
                    "Lon": "0",
                    "Confirmed": 297535,
                    "Deaths": 8498,
                    "Recovered": 147195,
                    "Active": 141842,
                    "Date": "2020-06-12T00:00:00Z"
                }
            ]
            """.trimIndent().toResponseBody()
    }

    private fun totalCountryDataResponse(): ResponseBody {
        return """
            [
                {
                    "Country": "Sample Country",
                    "CountryCode": "",
                    "Province": "",
                    "City": "",
                    "CityCode": "",
                    "Lat": "0",
                    "Lon": "0",
                    "Confirmed": 297535,
                    "Deaths": 8498,
                    "Recovered": 147195,
                    "Active": 141842,
                    "Date": "2020-06-12T00:00:00Z"
                },
                {
                    "Country": "Sample Country",
                    "CountryCode": "",
                    "Province": "",
                    "City": "",
                    "CityCode": "",
                    "Lat": "0",
                    "Lon": "0",
                    "Confirmed": 308993,
                    "Deaths": 8884,
                    "Recovered": 154330,
                    "Active": 145779,
                    "Date": "2020-06-13T00:00:00Z"
                }
            ]
            """.trimIndent().toResponseBody()
    }

    private fun countryResponse(): ResponseBody {
        return """
            [
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
            ]
            """.trimIndent().toResponseBody()
    }
}