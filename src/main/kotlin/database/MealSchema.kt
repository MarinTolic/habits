package com.example.database

import com.example.model.Meal
import com.example.model.NutritionalValue
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class MealService(database: Database) {

    object Meals : Table() {
        val id = integer("id").autoIncrement()
        val uuid = varchar("uuid", 255)
        val name = varchar("name", length = 50)
        val quantity = integer("quantity")

        val averageEnergy = integer("averageEnergy")
        val averageProtein = integer("averageProtein")
        val averageCarbohydrate = integer("averageCarbohydrate")
        val averageFat = integer("averageFat")
    }

    init {
        transaction(database) {
            SchemaUtils.create(Meals)
        }
    }

    suspend fun create(meal: Meal): Int = dbQuery {
        Meals.insert {
            it[name] = meal.name
            it[uuid] = meal.uuid
            it[quantity] = meal.weight
            it[averageEnergy] = meal.averageNutritionalValue.energy
            it[averageProtein] = meal.averageNutritionalValue.protein
            it[averageCarbohydrate] = meal.averageNutritionalValue.carbohydrates
            it[averageFat] = meal.averageNutritionalValue.fat
        }[Meals.id]
    }

    suspend fun read(id: Int): Meal? {
        return dbQuery {
            Meals.selectAll()
                .where { Meals.id eq id }
                .map { it.toMeal() }
                .singleOrNull()
        }
    }

    suspend fun readAll(): List<Meal> {
        return dbQuery {
            Meals.selectAll()
                .map { it.toMeal() }
        }
    }

    suspend fun update(id: Int, meal: Meal) {
        dbQuery {
            Meals.update({ Meals.id eq id }) {
                it[name] = meal.name
                it[uuid] = meal.uuid
                it[quantity] = meal.weight
                it[averageEnergy] = meal.averageNutritionalValue.energy
                it[averageProtein] = meal.averageNutritionalValue.protein
                it[averageCarbohydrate] = meal.averageNutritionalValue.carbohydrates
                it[averageFat] = meal.averageNutritionalValue.fat
            }
        }
    }

    suspend fun delete(id: Int) {
        dbQuery {
            Meals.deleteWhere { Meals.id.eq(id) }
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    private fun ResultRow.toMeal() = Meal(
        uuid = this[Meals.uuid],
        name = this[Meals.name],
        weight = this[Meals.quantity],
        averageNutritionalValue = NutritionalValue(
            energy = this[Meals.averageEnergy],
            protein = this[Meals.averageProtein],
            carbohydrates = this[Meals.averageCarbohydrate],
            fat = this[Meals.averageFat]
        )
    )
}