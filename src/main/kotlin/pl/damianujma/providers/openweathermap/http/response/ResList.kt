package pl.damianujma.providers.openweathermap.http.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResList (

    @SerialName("dt") val dt : Int,
    @SerialName("main") val main : Main,
    @SerialName("weather") val weather : List<Weather>,
    @SerialName("clouds") val clouds : Clouds,
    @SerialName("wind") val wind : Wind,
    @SerialName("visibility") val visibility : Int,
    @SerialName("pop") val pop : Double,
    @SerialName("sys") val sys : Sys,
    @SerialName("dt_txt") val dt_txt : String
)