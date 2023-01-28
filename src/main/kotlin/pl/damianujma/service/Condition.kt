package pl.damianujma.service

import kotlinx.serialization.Serializable

@Serializable
data class Condition(
    val alarmConditionsId: Long,
    val maxTemp: Double,
    val minTemp: Double,
    val city: String
)