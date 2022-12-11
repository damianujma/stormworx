package pl.damianujma.providers.openweathermap.http.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Wind (

	@SerialName("speed") val speed : Double,
	@SerialName("deg") val deg : Int,
	@SerialName("gust") val gust : Double
)