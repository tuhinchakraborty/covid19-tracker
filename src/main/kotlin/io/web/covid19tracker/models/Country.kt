package io.web.covid19tracker.models

import com.fasterxml.jackson.annotation.JsonProperty

data class Country(
        @JsonProperty("Country") val country: String,
        @JsonProperty("Slug") val slug: String,
        @JsonProperty("ISO2") val iso2: String
)