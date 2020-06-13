package io.web.covid19tracker.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.web.covid19tracker.config.ApiAppConfig
import io.web.covid19tracker.models.Country
import io.web.covid19tracker.models.CountryData
import io.web.covid19tracker.util.getResponseFor
import io.web.covid19tracker.util.getResponseForDates
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class CountryService(@Autowired apiAppConfig: ApiAppConfig) {

    private val apiBaseUrl = apiAppConfig.baseUrl
    private val countriesUri = apiAppConfig.countries
    private val totalAllStatus = apiAppConfig.totalAllStatus

    private val jacksonObjectMapper = jacksonObjectMapper()

    fun getCountries(): List<Country>? {
        val countryJson = getResponseFor(apiBaseUrl, countriesUri)?.string()
        return jacksonObjectMapper.readValue(countryJson, object : TypeReference<List<Country>>() {})
    }

    fun getCountry(
            countries: List<Country>?,
            countryName: String?
    ): Country? {
        return countries?.first {
            it.country == countryName
        }
    }

    fun getTotalCountryData(countrySlug: String): List<CountryData> {
        val totalCountryData = getResponseFor(apiBaseUrl, totalAllStatus + countrySlug)?.string()
        return jacksonObjectMapper.readValue(totalCountryData, object : TypeReference<List<CountryData>>() {})
    }

    fun getCurrentCountryData(countrySlug: String): CountryData {
        val currentDate = LocalDate.now()
        val previousDate = currentDate.minusDays(1)
        val countryData = getResponseForDates(
                apiBaseUrl,
                totalAllStatus + countrySlug,
                previousDate.toString(),
                currentDate.toString()
        )?.string()
        return jacksonObjectMapper.readValue(countryData, object : TypeReference<List<CountryData>>() {}).first()
    }
}