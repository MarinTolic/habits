package com.example

import com.example.database.configureDatabases
import com.example.plugins.configureContentNegotiation
import com.example.routing.configureRouting
import io.ktor.server.application.*

/**
 * The entry point for the application.
 */
fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

/**
 * The main application module.
 */
fun Application.module() {
    configureContentNegotiation()
    configureDatabases()
    configureRouting()
}