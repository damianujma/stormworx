package pl.damianujma.repo

import arrow.core.Either
import org.jetbrains.exposed.dao.id.IntIdTable
import pl.damianujma.DomainError

@JvmInline value class AlarmConditionsId(val serial: Long)

interface AlarmConditionsPersistence {
    suspend fun insert(maxTemp: Double, city: String): Either<DomainError, AlarmConditionsId>
}

fun alarmConditionsPersistence() = object : AlarmConditionsPersistence{
    override suspend fun insert(maxTemp: Double, city: String): Either<DomainError, AlarmConditionsId> {
        TODO("Not yet implemented")
    }
}

object AlarmConditions: IntIdTable() {
    val maxTemp = double("maxTemp")
    val city = varchar("city", 50)
}