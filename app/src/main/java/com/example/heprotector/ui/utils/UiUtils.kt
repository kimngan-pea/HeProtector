package com.example.heprotector.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

interface ChartEntry {
    val time: Long
    val value: Float
}

@Composable
fun screenHeightDp(): Dp {
    return LocalConfiguration.current.screenHeightDp.dp
}
fun List<ChartEntry>.toChartData(): List<Pair<Float, Float>> {
    return this.sortedBy { it.time }.mapIndexed { index, item ->
        index.toFloat() to item.value
    }
}
fun calculateBMI(weight: Float, height: Float): Float {
    if (height <= 0f) return 0f
    val heightInMeters = height / 100
    return weight / (heightInMeters * heightInMeters)
}

fun getTodayShortDay(): String {
    val calendar = Calendar.getInstance()
    val sdf = SimpleDateFormat("EE", Locale.ENGLISH)
    return when (sdf.format(calendar.time)) {
        "Mon" -> "MO"
        "Tue" -> "TU"
        "Wed" -> "WE"
        "Thu" -> "TH"
        "Fri" -> "FR"
        "Sat" -> "SA"
        "Sun" -> "SU"
        else -> "MO"
    }
}

fun currentDateString(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(Date())
}

fun parseDateToMillis(dateString: String): Long {
    return try {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = formatter.parse(dateString)
        date?.time ?: 0L
    } catch (e: Exception) {
        0L
    }
}
