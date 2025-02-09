package com.example.routing

import com.example.database.MealService
import com.example.database.mealService
import com.example.model.Meal
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Configures the routing for the server
 */
fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello Android Dev Academy!")
        }

        mealRoutes(mealService)
    }
}

/**
 * Contains the routes used for meal CRUD operations.
 *
 * @param mealService The service used for CRUD interactions with the server database.
 */
private fun Routing.mealRoutes(mealService: MealService) {

    /**
     * Route used to create a meal.
     */
    post("/meals") {
        val meal = call.receive<Meal>()
        val id = mealService.create(meal)
        call.respond(HttpStatusCode.Created, id)
    }

    /**
     * Route used to fetch all meals.
     */
    get("/meals/all") {
        val meals = mealService.readAll()

        call.respond(HttpStatusCode.OK, meals)
    }

    /**
     * Route used to fetch a single meal by id.
     */
    get("/meals/{id}") {
        val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
        val meal = mealService.read(id)
        if (meal != null) {
            call.respond(HttpStatusCode.OK, meal)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }

    /**
     * Route used to update the meal with the given id.
     */
    put("/meals/{id}") {
        val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
        val meal = call.receive<Meal>()
        mealService.update(id, meal)
        call.respond(HttpStatusCode.OK)
    }

    /**
     * Route used to delete the meal with the given id.
     */
    delete("/meals/{id}") {
        val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
        mealService.delete(id)
        call.respond(HttpStatusCode.OK)
    }
}
