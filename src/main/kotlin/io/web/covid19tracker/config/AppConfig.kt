package io.web.covid19tracker.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@ConfigurationProperties(prefix = "covid19")
@Configuration
data class AppConfig(var baseUrl: String = "", var dataUri: String = "")