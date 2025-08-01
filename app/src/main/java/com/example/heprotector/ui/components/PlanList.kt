import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.heprotector.ui.conponents.PlanItemRow

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlanList(
    items: List<Any>,
    onRemoveItem: (Any) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = items,
            key = { it.hashCode() }
        ) { item ->
            val dismissState = rememberDismissState(
                confirmStateChange = { dismissValue ->
                    if (dismissValue == DismissValue.DismissedToStart) {
                        onRemoveItem(item)
                    }
                    true
                }
            )

            //swip to delete items
            SwipeToDismiss(
                state = dismissState,
                directions = setOf(DismissDirection.EndToStart),
                background = {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Transparent),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .width(80.dp)
                                .fillMaxHeight()
                                .background(Color.White.copy(alpha = 0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.Red,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                },
                dismissContent = {
                    when (item) {
                        is com.example.heprotector.model.MealItem -> {
                            PlanItemRow(
                                title = item.name,
                                info = item.kcal.toString(),
                                unit = item.unit,
                                liked = true
                            )
                        }
                        is com.example.heprotector.data.model.ExerciseItem -> {
                            PlanItemRow(
                                title = item.name,
                                info = item.duration.toString(),
                                unit = item.unit,
                                liked = true
                            )
                        }

                        else -> {}
                    }
                }
            )
        }
    }
}

inline fun <reified T> removeAndResequenceIds(
    list: List<T>,
    target: T,
    getId: (T) -> Int,
    updateId: (T, Int) -> T
): List<T> {
    val updatedList = list.toMutableList().apply {
        remove(target)
    }

    val maxOriginalId = list.maxOfOrNull { getId(it) } ?: 0
    val maxAfterRemove = updatedList.maxOfOrNull { getId(it) } ?: 0
    if (maxAfterRemove == maxOriginalId - 1) return updatedList


    return updatedList.mapIndexed { index, item ->
        updateId(item, index + 1)
    }
}
