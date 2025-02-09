package com.example.database

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

lateinit var mealService: MealService

fun configureDatabases() {
    val database = Database.connect(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
        user = "root",
        driver = "org.h2.Driver",
        password = "",
    )

    mealService = MealService(database)
}