import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DayTabRow(
    selectedDay: String,
    onDaySelected: (String) -> Unit
) {
    var selected by remember { mutableStateOf(selectedDay) }
    val days = listOf("MO", "TU", "WE", "TH", "FR", "SA", "SU")

    Row(modifier = Modifier.fillMaxWidth()) {
        days.forEach { day ->
            val isSelected = day == selected
            val backgroundColor = if (isSelected) Color(0xFF3674B5) else Color(0xFFE0F7FA)
            val textColor = if (isSelected) Color.White else Color(0xFF3674B5)

            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clickable {
                        selected = day
                        onDaySelected(day)
                    }
                    .background(backgroundColor)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = day, fontSize = 16.sp, color = textColor)
            }
        }
    }
}
