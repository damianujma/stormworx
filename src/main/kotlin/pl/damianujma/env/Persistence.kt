package pl.damianujma.env

import arrow.core.Either
import org.jetbrains.exposed.sql.Database
import pl.damianujma.ConnectionError
import pl.damianujma.DatabaseConnectionError
import pl.damianujma.Unexpected

fun sqlSetup(dataSource: Env.DataSource): Either<ConnectionError, Database> {
    return Either.catch {  Database.connect(url = dataSource.url, driver = dataSource.driver, user = dataSource.username, password = dataSource.password) }
        .mapLeft { error ->
            if (error is IllegalStateException) {
                DatabaseConnectionError(error.message ?: "No error message")
            } else {
                Unexpected("Failed to connect to database", error)
            }
        }
}