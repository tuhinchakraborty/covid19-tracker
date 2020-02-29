package io.web.covid19tracker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Covid19TrackerApplication

fun main(args: Array<String>) {
	runApplication<Covid19TrackerApplication>(*args)
}
