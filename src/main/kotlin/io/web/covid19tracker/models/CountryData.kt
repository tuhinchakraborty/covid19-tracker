package io.web.covid19tracker.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class CountryData(
        @JsonProperty("Confirmed") val confirmed: Int,
        @JsonProperty("Deaths") val deaths: Int,
        @JsonProperty("Recovered") val recovered: Int,
        @JsonProperty("Active") val active: Int,
        @JsonProperty("Date") val date: String
)
