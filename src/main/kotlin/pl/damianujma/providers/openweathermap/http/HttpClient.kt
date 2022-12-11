package pl.damianujma.providers.openweathermap.http

import arrow.core.Either
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import pl.damianujma.DomainError
import pl.damianujma.providers.openweathermap.http.response.OpenWeatherMapResponse

val client = HttpClient(CIO)
val appId = ""

val json = Json { ignoreUnknownKeys = true }

suspend fun HttpClient.fetchResponse(city: String, appId: String): Either<DomainError, OpenWeatherMapResponse> {
    val apiRequest = "https://api.openweathermap.org/data/2.5/forecast?q=$city&appid=$appId&units=metric"
    return json.decodeFromString(this.get(apiRequest).body())
}