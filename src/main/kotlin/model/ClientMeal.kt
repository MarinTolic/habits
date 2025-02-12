package com.example.model

import kotlinx.serialization.Serializable
import java.util.UUID

/**
 * Represents a single meal the user has consumed as sent from the client, without server assigned ID.
 *
 * @param name The name of the meal.
 * @param weight The weight of the meal, expressed in grams.
 * @param averageNutritionalValue The nutritional value of the meal expressed as an average, per 100g.
 */
@Serializable
data class ClientMeal(
    val name: String,
    val weight: Int,
    val averageNutritionalValue: NutritionalValue,
) {

    /**
     * The total nutritional value of the meal.
     */
    val totalNutritionalValue: NutritionalValue = averageNutritionalValue.times(weight)

    fun toMeal() = Meal(
        uuid = UUID.randomUUID().toString(),
        name = this.name,
        weight = this.weight,
        averageNutritionalValue = averageNutritionalValue,
    )
}