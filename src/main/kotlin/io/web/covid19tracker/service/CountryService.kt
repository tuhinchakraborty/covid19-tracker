package io.web.covid19tracker.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.web.covid19tracker.config.ApiAppConfig
import io.web.covid19tracker.models.Country
import io.web.covid19tracker.util.getResponseFor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CountryService(@Autowired apiAppConfig: ApiAppConfig) {

    private val apiBaseUrl = apiAppConfig.baseUrl
    private val countriesUri = apiAppConfig.countries

    private val jacksonObjectMapper = jacksonObjectMapper()

    fun getCountries(): List<Country>? {
        val countryJson = getResponseFor(apiBaseUrl + countriesUri)?.string()
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
}