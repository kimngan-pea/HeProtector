package com.example.heprotector.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.heprotector.ui.utils.ChartEntry

@Entity(tableName = "blood_sugar")
data class BloodSugar(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int, // add userId
    override val value: Float,
    override val time: Long,
    val type: String
) : ChartEntry
