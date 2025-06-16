package soltani.code.taskvine.ui.component

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import soltani.code.taskvine.helpers.LocalCustomColors

//Data class for bottom navigation items
data class BottomNavigationItem(
    val name: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun BottomNavigation(
    modifier: Modifier = Modifier,
    items: List<BottomNavigationItem>,
    currentRoute: String,
    onItemSelected: (BottomNavigationItem) -> Unit
) {
    val colors = LocalCustomColors.current


    NavigationBar(containerColor = colors.primaryBackground) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onItemSelected(item) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.name
                    )
                },
                label = { Text(item.name) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = colors.SecondaryBackground,
                    selectedIconColor = colors.primaryText,
                    unselectedIconColor = colors.primaryText,
                    selectedTextColor = colors.primaryText,
                    unselectedTextColor = colors.primaryText
                )
            )
        }
    }

}