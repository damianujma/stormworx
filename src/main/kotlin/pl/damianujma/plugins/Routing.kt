package pl.damianujma.plugins

import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import pl.damianujma.env.Dependencies

fun Application.configureRouting(dependencies: Dependencies) {

    routing {
        get("/hello") {
            call.respondText("Hello World!")
        }
    }
}
