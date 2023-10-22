package pl.damianujma.service

import arrow.core.Either
import arrow.core.computations.either
import pl.damianujma.ConnectionError
import pl.damianujma.providers.openweathermap.http.appId
import pl.damianujma.providers.openweathermap.http.client
import pl.damianujma.providers.openweathermap.http.fetchResponse
import pl.damianujma.repo.AlarmConditionsId
import pl.damianujma.repo.AlarmConditionsPersistence
import pl.damianujma.service.prediction.Prediction
import java.text.SimpleDateFormat
import java.util.*

@kotlinx.serialization.Serializable
data class CreateCondition(val maxTemp: Double, val minTemp: Double, val city: String, val email: String)
@kotlinx.serialization.Serializable
data class EmailParameter(val email: String)

interface AlarmConditionsService {
    suspend fun createCondition(input: CreateCondition): Either<ConnectionError, AlarmConditionsId>
    suspend fun getCondition(id: Long): Either<ConnectionError, Condition>
    suspend fun getMatchingPredictions(id: Long): Either<ConnectionError, List<Prediction>>
    suspend fun getMatchingPredictionsByEmail(email: String): Either<ConnectionError, Pair<String, List<Prediction>>>
}

fun alarmConditionsService(repo: AlarmConditionsPersistence) =
    object : AlarmConditionsService {
        override suspend fun createCondition(input: CreateCondition): Either<ConnectionError, AlarmConditionsId> =
            either {
                repo.insert(input.maxTemp, input.minTemp, input.city, input.email).bind()
            }

        override suspend fun getCondition(id: Long): Either<ConnectionError, Condition> = either {
            repo.get(id).bind()
        }

        override suspend fun getMatchingPredictions(id: Long): Either<ConnectionError, List<Prediction>> = either {
            val condition = getCondition(id).bind()
            val weather = fetchResponse(condition.city, appId, client).bind()
            weather.list
                .filter { prediction -> prediction.main.temp <= condition.minTemp || prediction.main.temp >= condition.maxTemp }
                .map { prediction ->
                    Prediction(
                        getDateTime(prediction.dt),
                        prediction.main.temp,
                        prediction.weather[0].main
                    )
                }
        }

        override suspend fun getMatchingPredictionsByEmail(email: String): Either<ConnectionError, Pair<String, List<Prediction>>> = either {
            val condition = repo.getByEmail(email).bind()
            val weather = fetchResponse(condition.city , appId, client).bind()
            condition.city to weather.list
                .filter { prediction -> prediction.main.temp <= condition.minTemp || prediction.main.temp >= condition.maxTemp }
                .map { prediction ->
                    Prediction(
                        getDateTime(prediction.dt),
                        prediction.main.temp,
                        prediction.weather[0].main
                    )
                }
        }
    }

private fun getDateTime(s: Int): String? {
    return try {
        val sdf = SimpleDateFormat("EEEE dd/M/yyyy hh:mm:ss")
        val netDate = Date(s.toLong() * 1000)
        sdf.format(netDate)
    } catch (e: Exception) {
        e.toString()
    }
}