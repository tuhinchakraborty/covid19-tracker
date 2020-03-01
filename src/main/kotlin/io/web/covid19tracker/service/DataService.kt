package io.web.covid19tracker.service

import io.web.covid19tracker.config.AppConfig
import io.web.covid19tracker.models.Data
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
class DataService(@Autowired appConfig: AppConfig) {
    val baseUrl = appConfig.baseUrl
    val dataUri = appConfig.dataUri
    val webClient = WebClient.builder().baseUrl(baseUrl).build()

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
            val currentCount = it.get(it.size() - 1).toInt()
            val previousCount = it.get(it.size() - 2).toInt()
            Data(province, country, currentCount, previousCount)
        }
    }
}