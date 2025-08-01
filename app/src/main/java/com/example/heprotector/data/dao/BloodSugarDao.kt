package com.example.heprotector.data.dao

import androidx.room.*
import com.example.heprotector.data.model.BloodSugar
import kotlinx.coroutines.flow.Flow

@Dao
interface BloodSugarDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bloodSugar: BloodSugar)

    @Query("SELECT * FROM blood_sugar WHERE userId = :userId ORDER BY time DESC")
    fun getByUserId(userId: Int): Flow<List<BloodSugar>>
}
