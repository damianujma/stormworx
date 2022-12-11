package pl.damianujma.providers.openweathermap.http.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Clouds (

	@SerialName("all") val all : Int
)