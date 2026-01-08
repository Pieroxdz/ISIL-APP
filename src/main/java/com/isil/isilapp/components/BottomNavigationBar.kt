package com.isil.isilapp.components

import android.content.Context
import android.content.Intent
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector
import com.isil.isilapp.EventsActivity
import com.isil.isilapp.MainActivity
import com.isil.isilapp.NewsActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import com.isil.isilapp.CategoriasClubsActivity
import com.isil.isilapp.ClubsActivity
import com.isil.isilapp.ProfileActivity

data class BottomNavItem(
    val label: String,
    val icon: ImageVector
)

@Composable
fun BottomNavigationBar(
    selectedIndex: Int = 0,
    onItemClick: (Int) -> Unit,
    context: Context
) {
    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home),
        BottomNavItem("Events", Icons.Default.Event),
        BottomNavItem("Orgs", Icons.Default.People),
        BottomNavItem("News", Icons.Default.Article),
        BottomNavItem("Profile", Icons.Default.Person)
    )

    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color.Gray,
        elevation = 8.dp
    ) {
        items.forEachIndexed { index, item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (selectedIndex == index)  MaterialTheme.colorScheme.primary else Color.Gray
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        color = if (selectedIndex == index)  MaterialTheme.colorScheme.primary else Color.Gray
                    )
                },
                selected = selectedIndex == index,
                onClick = {
                    onItemClick(index)
                    when (index) {
                        0 -> {
                            val intent = Intent(context, MainActivity::class.java)
                            context.startActivity(intent)
                        }
                        1 -> {
                            val intent = Intent(context, EventsActivity::class.java)
                            context.startActivity(intent)
                        }
                        2 -> {
                            val intent = Intent(context, CategoriasClubsActivity::class.java)
                            context.startActivity(intent
                            )
                        }
                        3 -> {
                            val intent = Intent(context, NewsActivity::class.java)
                            context.startActivity(intent)
                        }
                        4 -> {
                            val intent = Intent(context, ProfileActivity::class.java)
                            context.startActivity(intent)
                        }
                    }
                },
                alwaysShowLabel = true
            )
        }
    }
}
