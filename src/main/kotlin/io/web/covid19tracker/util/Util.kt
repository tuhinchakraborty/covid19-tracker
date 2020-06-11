package io.web.covid19tracker.util

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody

private val client = OkHttpClient()

fun getResponseFor(url: String): ResponseBody? {
    val request = Request
            .Builder()
            .url(url)
            .get()
            .build()
    val httpClient = client.newBuilder().build()
    val response = httpClient.newCall(request).execute()
    return response.body
}
