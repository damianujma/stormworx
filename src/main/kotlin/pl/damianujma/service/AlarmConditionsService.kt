package pl.damianujma.service

import arrow.core.Either
import arrow.core.computations.either
import pl.damianujma.DomainError
import pl.damianujma.repo.AlarmConditionsId
import pl.damianujma.repo.AlarmConditionsPersistence

@kotlinx.serialization.Serializable
data class CreateCondition(val maxTemp: Double,val city: String)

interface AlarmConditionsService {
    suspend fun createCondition(input: CreateCondition): Either<DomainError, AlarmConditionsId>
}

fun alarmConditionsService(repo: AlarmConditionsPersistence) =
    object : AlarmConditionsService {
        override suspend fun createCondition(input: CreateCondition): Either<DomainError, AlarmConditionsId> = either {
            repo.insert(input.maxTemp, input.city).bind()
        }
}