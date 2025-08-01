package com.example.heprotector.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.heprotector.ui.utils.ChartEntry

@Entity(tableName = "hba1c")
data class HbA1c(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    override val value: Float,
    override val time: Long
) : ChartEntry
