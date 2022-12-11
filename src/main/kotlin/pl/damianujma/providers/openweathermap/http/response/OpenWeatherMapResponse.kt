package pl.damianujma.providers.openweathermap.http.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenWeatherMapResponse (

	@SerialName("cod") val cod : Int,
	@SerialName("message") val message : Int,
	@SerialName("cnt") val cnt : Int,
	@SerialName("list") val list : List<ResList>,
	@SerialName("city") val city : City
)