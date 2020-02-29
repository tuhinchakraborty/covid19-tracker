package io.web.covid19tracker.service

import io.web.covid19tracker.config.AppConfig
import io.web.covid19tracker.models.Data
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
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
    fun fetchData(): Mono<String> {
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

        val parsedDataList = parseCSV(responseEntity)

        val bodyToMono = webClient
                .get()
                .retrieve()
                .bodyToMono(String::class.java)
        return bodyToMono
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
            Data(province, country, latestCount)
        }
    }
}