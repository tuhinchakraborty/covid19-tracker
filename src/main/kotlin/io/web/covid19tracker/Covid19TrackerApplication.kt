package io.web.covid19tracker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class Covid19TrackerApplication

fun main(args: Array<String>) {
	runApplication<Covid19TrackerApplication>(*args)
}
