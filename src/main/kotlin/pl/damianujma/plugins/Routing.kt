package pl.damianujma.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import pl.damianujma.service.AlarmConditionsService
import pl.damianujma.service.CreateCondition

fun Application.configureRouting(service: AlarmConditionsService) {

    routing {
        get("/hello") {
            call.respondText("Hello World!")
        }

        post("/alarmConditions/add") {
            val ctx = this.context
            ctx.receive<CreateCondition>().apply {
                service.createCondition(this)
                    .map { createCondition ->  ctx.respond(createCondition.serial)}
                    .mapLeft { errorMsg -> ctx.respond(HttpStatusCode.InternalServerError, errorMsg) }
            }
        }

        get("/alarmConditions/{id}") {
            service.getCondition(call.parameters["id"]?.toLong()!!)
                .map { condition -> call.respond(condition) }
                .mapLeft { call.respond(HttpStatusCode.NotFound)}
        }
    }
}
