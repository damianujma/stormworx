package pl.damianujma.repo

import arrow.core.Either
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.transaction
import pl.damianujma.DomainError
import pl.damianujma.UnexpectedDomainError

@JvmInline
value class AlarmConditionsId(val serial: Long)

interface AlarmConditionsPersistence {
    suspend fun insert(maxTemp: Double, city: String): Either<DomainError, AlarmConditionsId>
}

fun alarmConditionsPersistence() = object : AlarmConditionsPersistence {
    init {
        transaction {
            SchemaUtils.create(AlarmConditions)
        }
    }

    override suspend fun insert(maxTemp: Double, city: String): Either<DomainError, AlarmConditionsId> {
        return Either.catch {
            AlarmConditionsId(transaction {
                AlarmConditions.insertAndGetId { row ->
                    row[AlarmConditions.maxTemp] = maxTemp
                    row[AlarmConditions.city] = city
                }.value
            })
        }
            .mapLeft { error ->
                UnexpectedDomainError("Failed to connect to insert AlarmConditions", error)
            }

    }
}


internal object AlarmConditions : LongIdTable() {
    val maxTemp = double("maxTemp")
    val city = varchar("city", 50)
}