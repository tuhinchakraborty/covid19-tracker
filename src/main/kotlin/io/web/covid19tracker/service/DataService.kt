package io.web.covid19tracker.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.web.covid19tracker.config.ApiAppConfig
import io.web.covid19tracker.config.AppConfig
import io.web.covid19tracker.models.Country
import io.web.covid19tracker.models.Data
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.io.Reader
import java.io.StringReader
import javax.annotation.PostConstruct

@Service
class DataService(
        @Autowired appConfig: AppConfig,
        @Autowired apiAppConfig: ApiAppConfig
) {
    private val baseUrl = appConfig.baseUrl
    private val dataUri = appConfig.dataUri
    private val webClient = WebClient.builder().baseUrl(baseUrl).build()

    private val apiBaseUrl = apiAppConfig.baseUrl
    private val countriesUri = apiAppConfig.countries

    private val client = OkHttpClient()
    private val jacksonObjectMapper = jacksonObjectMapper()

    @PostConstruct
    @Scheduled(cron = "* * * * * *")
    fun fetchData(): Mono<List<Data>> {
        val responseString = webClient
                .get()
                .uri(dataUri)
                .retrieve()
                .bodyToMono(String::class.java)

        return responseString.map { response ->
            parseCSV(response).sortedByDescending {
                it.currentCount
            }
        }
    }

    private fun parseCSV(responseEntity: String): List<Data> {
        val reader: Reader = StringReader(responseEntity)
        val records: Iterable<CSVRecord> = CSVFormat
                .DEFAULT
                .withFirstRecordAsHeader()
                .parse(reader)

        return records.map {
            val province = it["Province/State"]
            val country = it["Country/Region"]
            val currentCount = it.get(it.size() - 1).toIntOrNull()
            val previousCount = it.get(it.size() - 2).toIntOrNull()
            Data(province, country, currentCount, previousCount)
        }
    }

    fun getCountryNames(): List<String> {
        val json = fetchCountries()?.string()
        val readValue = jacksonObjectMapper.readValue(json, object : TypeReference<List<Country>>() {})
        return readValue.map {
            it.country
        }
    }

    private fun fetchCountries(): ResponseBody? {
        val request = Request
                .Builder()
                .url(apiBaseUrl + countriesUri)
                .get()
                .build()
        val httpClient = client.newBuilder().build()
        val response = httpClient.newCall(request).execute()
        return response.body
    }
}