package io.web.covid19tracker.util

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody

private val client = OkHttpClient()

fun getResponseFor(url: String, path: String): ResponseBody? {
    val httpUrl = HttpUrl.Builder()
            .scheme("https")
            .host(url)
            .addPathSegment(path)
            .build()

    val request = Request
            .Builder()
            .url(httpUrl)
            .get()
            .build()
    val httpClient = client.newBuilder().build()
    val response = httpClient.newCall(request).execute()
    return response.body
}

fun getResponseForDates(
        url: String,
        path: String,
        from: String,
        to: String
): ResponseBody? {
    val httpUrl = HttpUrl.Builder()
            .scheme("https")
            .host(url)
            .addPathSegment(path)
            .addQueryParameter("from", from)
            .addQueryParameter("to", to)
            .build()

    val request = Request
            .Builder()
            .url(httpUrl)
            .get()
            .build()
    val httpClient = client.newBuilder().build()
    val response = httpClient.newCall(request).execute()
    return response.body
}
