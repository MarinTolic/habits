package com.example.model

import kotlinx.serialization.Serializable

/**
* Represents a single meal the user has consumed.
 *
 * @param name The name of the meal.
 * @param weight The weight of the meal, expressed in grams.
 * @param averageNutritionalValue The nutritional value of the meal expressed as an average, per 100g.
*/
@Serializable
data class Meal(
    val name: String,
    val weight: Int,
    val averageNutritionalValue: NutritionalValue,
){

    /**
     * The total nutritional value of the meal.
     */
    val totalNutritionalValue: NutritionalValue = averageNutritionalValue.times(weight)
}