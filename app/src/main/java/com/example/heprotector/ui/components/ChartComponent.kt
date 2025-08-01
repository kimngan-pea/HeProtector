import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart

import com.patrykandpatrick.vico.compose.chart.line.lineChart

import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryModelOf
@Composable
fun LineChartSection(
    title: String,
    data: List<Pair<Float, Float>>, // (x, y)
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
        .padding(8.dp),
    fontSize: TextUnit = 16.sp
) {
    // limit display point to prevent data too big
    val maxPoints = 50
    val trimmedData = data.sortedBy { it.first }.takeLast(maxPoints)

    // create FloatEntry list for charts
    val entries = trimmedData.map { (x, y) -> FloatEntry(x, y) }
    val model = entryModelOf(entries)

    Column(
        modifier = modifier
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Chart(
            chart = lineChart(),
            model = model,
            startAxis = rememberStartAxis(),     //  Y
            bottomAxis = rememberBottomAxis(),   //  X
            modifier = Modifier
                .fillMaxSize()
                .height(200.dp),
        )
    }
}


