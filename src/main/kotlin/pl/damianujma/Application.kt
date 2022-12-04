package pl.damianujma

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import pl.damianujma.env.Env
import pl.damianujma.env.dependencies
import pl.damianujma.plugins.*

fun main(){
    println("hello")
    val env = Env()
    val dependencies = dependencies(env)
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureSerialization()
        configureRouting(dependencies)
    }
        .start(wait = true)
}


fun Application.app(): Application.() -> Unit = {

}
