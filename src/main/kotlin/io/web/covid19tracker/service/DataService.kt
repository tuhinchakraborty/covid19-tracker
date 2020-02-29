package io.web.covid19tracker.service

import io.web.covid19tracker.config.AppConfig
import io.web.covid19tracker.models.Data
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.io.Reader
import java.io.StringReader
import javax.annotation.PostConstruct

@Service
class DataService(@Autowired appConfig: AppConfig) {
    val dataUrl = appConfig.dataUrl

    @PostConstruct
    @Scheduled(cron = "* * * * * *")
    fun fetchData(): Mono<List<Data>> {
        val webClient = WebClient.create(dataUrl)
        val clientResponseMono = webClient
                .get()
                .exchange()

        return clientResponseMono.flatMap {
            it.toEntity(String::class.java)
        }.map {
            parseCSV(it)
        }
    }

    private fun parseCSV(responseEntity: ResponseEntity<String>?): List<Data> {
        val reader: Reader = StringReader(responseEntity?.body)
        val records: Iterable<CSVRecord> = CSVFormat
                .DEFAULT
                .withFirstRecordAsHeader()
                .parse(reader)

        return records.map {
            val province = it["Province/State"]
            val country = it["Country/Region"]
            val latestCount = it.get(it.size() - 1)
            Data(province, country, latestCount.toInt())
        }.sortedByDescending {
            it.currentCount
        }
    }
}