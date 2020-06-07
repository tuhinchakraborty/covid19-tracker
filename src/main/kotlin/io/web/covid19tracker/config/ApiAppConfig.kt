package io.web.covid19tracker.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@ConfigurationProperties(prefix = "covid19api")
@Configuration
data class ApiAppConfig(var baseUrl: String = "", var countries: String = "")