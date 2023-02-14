package pl.damianujma.plugins

import arrow.core.Either
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import pl.damianujma.ConnectionError
import pl.damianujma.UnexpectedConnectionError
import pl.damianujma.UnexpectedOpenWeatherMapConnectionError
import pl.damianujma.service.AlarmConditionsService
import pl.damianujma.service.CreateCondition

fun Application.configureRouting(service: AlarmConditionsService) {

    routing {
        post("/alarm-conditions/add") {
            val ctx = this.context
            ctx.receive<CreateCondition>().apply {
                when (val createCondition = service.createCondition(this)) {
                    is Either.Left -> {
                        val error = createCondition.value
                        if (error is UnexpectedConnectionError) {
                            call.respond(HttpStatusCode.InternalServerError, error.message)
                        } else if (error is UnexpectedOpenWeatherMapConnectionError) {
                            call.respond(HttpStatusCode.InternalServerError, error.message)
                        } else {
                            call.respond(HttpStatusCode.InternalServerError)
                        }
                    }
                    is Either.Right -> ctx.respond(createCondition.value.serial)
                }
            }
        }

        get("/alarm-conditions/{id}") {
            call.parameters["id"]?.let {
                when (val condition = service.getCondition(it.toLong())) {
                    is Either.Left -> handleDomainError(condition)
                    is Either.Right -> call.respond(condition.value)
                }
            } ?: call.respond(HttpStatusCode.NotAcceptable, "Parameter `id` cannot be null")
        }

        get("/alarm-conditions/{id}/predictions") {
            call.parameters["id"]?.let {
                when (val condition = service.getMatchingPredictions(it.toLong())) {
                    is Either.Left -> handleDomainError(condition)
                    is Either.Right -> call.respond(condition.value)
                }
            } ?: call.respond(HttpStatusCode.NotAcceptable, "Parameter `id` cannot be null")
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handleDomainError(
    condition: Either.Left<ConnectionError>
) {
    val error = condition.value
    if (error is UnexpectedConnectionError) {
        call.respond(HttpStatusCode.InternalServerError, error.message)
    } else if (error is UnexpectedOpenWeatherMapConnectionError) {
        call.respond(HttpStatusCode.InternalServerError, error.message)
    } else {
        call.respond(HttpStatusCode.InternalServerError)
    }
}
