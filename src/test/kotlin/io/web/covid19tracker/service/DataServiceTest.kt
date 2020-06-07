package io.web.covid19tracker.service

import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.web.covid19tracker.config.ApiAppConfig
import io.web.covid19tracker.config.AppConfig
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.test.StepVerifier

internal class DataServiceTest {

    @MockK
    private lateinit var appConfig: AppConfig

    @MockK
    private lateinit var apiAppConfig: ApiAppConfig

    @InjectMockKs
    private lateinit var dataService: DataService

    private lateinit var mockWebServer: MockWebServer

    @BeforeEach
    internal fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        val url = mockWebServer.url("/")
        appConfig = AppConfig(url.toString(), "/data")
        apiAppConfig = ApiAppConfig(url.toString(), "/api")
        dataService = DataService(appConfig, apiAppConfig)
        MockKAnnotations.init(this)
    }

    @AfterEach
    internal fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    internal fun shouldFetchData() {
        mockWebServer.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody("Province/State,Country/Region,Lat,Long,1/22/20,1/23/20,1/24/20,1/25/20,1/26/20\n" +
                        ",Afghanistan,33.0,65.0,100,118,124,130,136\n" +
                        ",Germany,51.0,9.0,180,181,185,190,200\n"))

        val fetchData = dataService.fetchData()
        val dataFlux = fetchData.flatMapMany {
            Flux.fromIterable(it)
        }

        StepVerifier.create(dataFlux)
                .expectNextMatches {
                    it.country == "Germany"
                            && it.currentCount == 200
                            && it.previousCount == 190
                }
                .expectNextMatches {
                    it.country == "Afghanistan"
                            && it.currentCount == 136
                            && it.previousCount == 130
                }
                .verifyComplete()
    }
}