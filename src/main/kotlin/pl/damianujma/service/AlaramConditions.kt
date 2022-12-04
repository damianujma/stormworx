package pl.damianujma.service

import kotlinx.serialization.Serializable

@Serializable
data class AlarmConditions(
    val alarmConditionsId: Long,
    val maxTemp: Double,
    val city: String
)