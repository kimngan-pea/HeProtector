import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.heprotector.ui.theme.Black
import com.example.heprotector.ui.theme.BlueExtraLight
import com.example.heprotector.ui.theme.BlueGray
import com.example.heprotector.ui.theme.CyanLight
import com.example.heprotector.ui.theme.greenGray


@Composable
fun BottomNavigationBar(navController: NavController, userId: Int?) {
    val items = listOf(
        NavigationItem("Home", Icons.Filled.Home, "home"),
        NavigationItem("History", Icons.Filled.BarChart, "history"),
        NavigationItem("Profile", Icons.Filled.Person, "profile")
    )

    NavigationBar(containerColor = greenGray) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            val isSelected = currentRoute?.startsWith(item.route) == true

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (isSelected) Black else CyanLight,
                        modifier = Modifier.size(26.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        color = if (isSelected) Black else CyanLight,
                        fontSize = 20.sp
                    )
                },
                selected = isSelected,
                onClick = {
                    if (!isSelected && userId != null) {
                        val targetRoute = when (item.route) {
                            "home" -> "home/$userId"
                            "history" -> "history/$userId"
                            "profile" -> "profile/$userId"
                            else -> item.route
                        }
                        navController.navigate(targetRoute) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = if (isSelected) BlueExtraLight else BlueGray,
                    selectedIconColor = Black,
                    selectedTextColor = Black,
                    unselectedIconColor = CyanLight,
                    unselectedTextColor = CyanLight
                )
            )
        }
    }
}

data class NavigationItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)