package pl.damianujma

import arrow.core.Either
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import pl.damianujma.env.Env
import pl.damianujma.env.dependencies
import pl.damianujma.plugins.configureRouting
import pl.damianujma.plugins.configureSerialization

fun main() {
    val env = Env()
    val dependencies = dependencies(env)
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureSerialization()
        when (dependencies) {
            is Either.Left -> throw Exception()
            is Either.Right -> configureRouting(dependencies.value.alarmConditionsService)
        }
    }
        .start(wait = true)
}
