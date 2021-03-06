package io.web.covid19tracker.models

data class Data(
        val province: String,
        val country: String,
        val currentCount: Int?,
        val previousCount: Int?
) {
    fun getDifference() = previousCount?.let { currentCount?.minus(it) }
}