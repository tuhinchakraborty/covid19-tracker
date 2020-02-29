package io.web.covid19tracker

import io.web.covid19tracker.config.AppConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller(@Autowired val appConfig: AppConfig) {

    @GetMapping("/test")
    fun testController(): String {
        return appConfig.dataUrl
    }
}