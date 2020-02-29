package io.web.covid19tracker.service

import io.web.covid19tracker.config.AppConfig
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.io.Reader
import java.io.StringReader
import javax.annotation.PostConstruct


@Service
class DataService(@Autowired appConfig: AppConfig) {
    val dataUrl = appConfig.dataUrl

    @PostConstruct
    @Scheduled(cron = "0 0 0/12 ? * * *")
    fun fetchData(): ResponseEntity<String>? {
        println("Called........")
        val webClient = WebClient
                .builder()
                .baseUrl(dataUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE)
                .build()

        val responseEntity = webClient
                .get()
                .exchange()
                .block()
                ?.toEntity(String::class.java)
                ?.block()
        parseCSV(responseEntity)
        return responseEntity
    }

    private fun parseCSV(responseEntity: ResponseEntity<String>?) {
        val reader: Reader = StringReader(responseEntity?.body)
        val records: Iterable<CSVRecord> = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)
        for (record in records) {
            val province = record["Province/State"]
            val country = record["Country/Region"]
        }
    }
}