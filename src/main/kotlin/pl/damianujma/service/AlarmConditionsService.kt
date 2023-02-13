package pl.damianujma.service

import arrow.core.Either
import arrow.core.computations.either
import pl.damianujma.DomainError
import pl.damianujma.providers.openweathermap.http.appId
import pl.damianujma.providers.openweathermap.http.client
import pl.damianujma.providers.openweathermap.http.fetchResponse
import pl.damianujma.providers.openweathermap.http.response.ResList
import pl.damianujma.repo.AlarmConditionsId
import pl.damianujma.repo.AlarmConditionsPersistence

@kotlinx.serialization.Serializable
data class CreateCondition(val maxTemp: Double, val minTemp: Double, val city: String)

interface AlarmConditionsService {
    suspend fun createCondition(input: CreateCondition): Either<DomainError, AlarmConditionsId>
    suspend fun getCondition(id: Long): Either<DomainError, Condition>
    suspend fun getMatchingPredictions(id: Long): Either<DomainError, List<ResList>>
}

fun alarmConditionsService(repo: AlarmConditionsPersistence) =
    object : AlarmConditionsService {
        override suspend fun createCondition(input: CreateCondition): Either<DomainError, AlarmConditionsId> = either {
            repo.insert(input.maxTemp, input.minTemp, input.city).bind()
        }

        override suspend fun getCondition(id: Long): Either<DomainError, Condition> = either {
            repo.get(id).bind()
        }

        override suspend fun getMatchingPredictions(id: Long): Either<DomainError, List<ResList>> = either {
            val condition = getCondition(id).bind()
            val weather = fetchResponse(condition.city, appId, client).bind()
            weather.list
                .filter { list -> list.main.temp_min <= condition.minTemp || list.main.temp_max >= condition.maxTemp }
        }
    }
