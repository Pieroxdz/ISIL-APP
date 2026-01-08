package com.isil.isilapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.isil.isilapp.components.SearchBar
import com.isil.isilapp.ui.theme.ISILAppTheme
import com.isil.isilapp.components.BottomNavigationBar

class EventsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ISILAppTheme {
                EventsScreen()
            }
        }
    }
}

@Composable
fun EventsScreen() {
    val context = LocalContext.current
    var selectedIndex by remember { mutableStateOf(1) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedIndex = selectedIndex,
                onItemClick = { index -> selectedIndex = index },
                context = context
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            SearchBar("Search - Event")
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Events",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            CategoryRow()
            Spacer(modifier = Modifier.height(16.dp))

            EventList()
            Spacer(modifier = Modifier.height(16.dp))

            MapButton()
        }
    }
}



// Puedes asignar un icono a cada categoría
val categoryIcons = mapOf(
    "Todos" to Icons.Default.List,
    "Deportes" to Icons.Default.SportsSoccer,
    "Tech" to Icons.Default.Computer,
    "Arte" to Icons.Default.Brush,
    "Dance" to Icons.Default.Favorite,
    "Juegos" to Icons.Default.VideogameAsset,
    "Estudio" to Icons.Default.School,
    "Más" to Icons.Default.MoreHoriz
)

@Composable
fun CategoryRow() {
    val categories = listOf(
        "Todos", "Deportes", "Tech", "Arte", "Dance", "Juegos", "Estudio" ,"Más"
    )
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(categories) { category ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary) // color de fondo de tu tema
                        .clickable { /* acción al tocar categoría */ },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = categoryIcons[category] ?: Icons.Default.Help,
                        contentDescription = category,
                        tint = Color.White // icono blanco
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(category, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun EventList() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // más adelante LazyColumn de EventCards
            .background(Color(0xFFF1F1F1), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text("Eventos aquí…")
    }
}

@Composable
fun MapButton() {
    Button(
        onClick = { /* abrir mapa */ },
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Text("Map")
    }
}
