package pl.damianujma.repo

import arrow.core.Either
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import pl.damianujma.DomainError
import pl.damianujma.UnexpectedDomainError
import pl.damianujma.service.Condition

@JvmInline
value class AlarmConditionsId(val serial: Long)

interface AlarmConditionsPersistence {
    suspend fun insert(maxTemp: Double, minTemp: Double, city: String): Either<DomainError, AlarmConditionsId>
    suspend fun get(id: Long): Either<DomainError, Condition>
}

fun alarmConditionsPersistence() = object : AlarmConditionsPersistence {
    init {
        transaction {
            SchemaUtils.create(AlarmConditions)
        }
    }

    override suspend fun insert(maxTemp: Double, minTemp: Double, city: String): Either<DomainError, AlarmConditionsId> {
        return Either.catch {
            AlarmConditionsId(transaction {
                AlarmConditions.insertAndGetId { row ->
                    row[AlarmConditions.maxTemp] = maxTemp
                    row[AlarmConditions.minTemp] = minTemp
                    row[AlarmConditions.city] = city
                }.value
            })
        }
            .mapLeft { error ->
                UnexpectedDomainError("Failed to insert AlarmConditions", error)
            }

    }

    override suspend fun get(id: Long): Either<DomainError, Condition> {
        return Either.catch {
            transaction {
                AlarmConditions.select( AlarmConditions.id eq id ).map {
                    row -> Condition(id, row[AlarmConditions.maxTemp],
                    row[AlarmConditions.minTemp], row[AlarmConditions.city])
                }[0]
            }
        }
            .mapLeft { error ->
                UnexpectedDomainError("Failed to get AlarmConditions", error)
            }

    }
}


internal object AlarmConditions : LongIdTable() {
    val maxTemp = double("maxTemp")
    val minTemp = double("minTemp")
    val city = varchar("city", 50)
}