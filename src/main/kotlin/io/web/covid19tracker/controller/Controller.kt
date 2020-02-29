package io.web.covid19tracker.controller

import io.web.covid19tracker.config.AppConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class Controller(@Autowired val appConfig: AppConfig) {

    fun home(): String {
        return "home"
    }
}