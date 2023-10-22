package pl.damianujma.repo

import arrow.core.Either
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import pl.damianujma.ConnectionError
import pl.damianujma.UnexpectedConnectionError
import pl.damianujma.service.Condition

@JvmInline
value class AlarmConditionsId(val serial: Long)

interface AlarmConditionsPersistence {
    suspend fun insert(maxTemp: Double, minTemp: Double, city: String, email: String): Either<ConnectionError, AlarmConditionsId>
    suspend fun get(id: Long): Either<ConnectionError, Condition>
    suspend fun getByEmail(email: String): Either<ConnectionError, Condition>
}

fun alarmConditionsPersistence() = object : AlarmConditionsPersistence {
    init {
        transaction {
            SchemaUtils.create(AlarmConditions)
        }
    }

    override suspend fun insert(maxTemp: Double, minTemp: Double, city: String, email: String): Either<ConnectionError, AlarmConditionsId> {
        return Either.catch {
            AlarmConditionsId(transaction {
                AlarmConditions.insertAndGetId { row ->
                    row[AlarmConditions.maxTemp] = maxTemp
                    row[AlarmConditions.minTemp] = minTemp
                    row[AlarmConditions.city] = city
                    row[AlarmConditions.email] = email
                }.value
            })
        }
            .mapLeft { error ->
                UnexpectedConnectionError("Failed to insert AlarmConditions", error)
            }

    }

    override suspend fun get(id: Long): Either<ConnectionError, Condition> {
        return Either.catch {
            transaction {
                AlarmConditions.select( AlarmConditions.id eq id ).map {
                    row -> Condition(id, row[AlarmConditions.maxTemp],
                    row[AlarmConditions.minTemp], row[AlarmConditions.city], row[AlarmConditions.email])
                }[0]
            }
        }
            .mapLeft { error ->
                UnexpectedConnectionError("Failed to get AlarmConditions", error)
            }

    }

    override suspend fun getByEmail(email: String): Either<ConnectionError, Condition> {
        return Either.catch {
            transaction {
                AlarmConditions.select( AlarmConditions.email eq email ).map {
                        row -> Condition(row[AlarmConditions.id].value, row[AlarmConditions.maxTemp],
                    row[AlarmConditions.minTemp], row[AlarmConditions.city], row[AlarmConditions.email])
                }
            }[0]
        }
            .mapLeft { error ->
                UnexpectedConnectionError("Failed to get AlarmConditions", error)
            }

    }
}


internal object AlarmConditions : LongIdTable() {
    val maxTemp = double("maxTemp")
    val minTemp = double("minTemp")
    val city = varchar("city", 50)
    val email = varchar("email", 50)
}