package com.example.heprotector_.data.repository

import com.example.heprotector_.data.db.dao.LocalFoodDao
import com.example.heprotector_.data.db.entities.Local_Food
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LocalFoodRepository(private val localFoodDao: LocalFoodDao) {

    /**
     * Inserts a new local food item.
     * @param food The Local_Food object to insert.
     */
    suspend fun insertLocalFood(food: Local_Food) =
        withContext(Dispatchers.IO) {
            localFoodDao.insertLocalFood(food)
        }

    /**
     * Retrieves all local food items, ordered by food name.
     * @return A Flow emitting a list of Local_Food objects.
     */
    fun getAllLocalFoods(): Flow<List<Local_Food>> {
        return localFoodDao.getAllLocalFoods()
    }

    /**
     * Searches for local food items by name.
     * @param query The search string.
     * @return A Flow emitting a list of Local_Food objects matching the query.
     */
    fun searchLocalFoods(query: String): Flow<List<Local_Food>> {
        return localFoodDao.searchLocalFoods(query)
    }
}