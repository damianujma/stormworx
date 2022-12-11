package pl.damianujma.providers.openweathermap.http.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Main (

	@SerialName("temp") val temp : Double,
	@SerialName("feels_like") val feels_like : Double,
	@SerialName("temp_min") val temp_min : Double,
	@SerialName("temp_max") val temp_max : Double,
	@SerialName("pressure") val pressure : Int,
	@SerialName("sea_level") val sea_level : Int,
	@SerialName("grnd_level") val grnd_level : Int,
	@SerialName("humidity") val humidity : Int,
	@SerialName("temp_kf") val temp_kf : Double
)

