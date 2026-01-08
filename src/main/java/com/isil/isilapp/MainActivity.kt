package com.isil.isilapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.clickable
import android.content.Intent
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.LaunchedEffect


//COMPONENTES
import com.isil.isilapp.components.NewsCard
import com.isil.isilapp.components.SearchBar
import com.isil.isilapp.components.BottomNavigationBar
import com.isil.isilapp.components.SedeCard
import com.isil.isilapp.models.Sede
import com.isil.isilapp.ui.theme.ISILAppTheme
import com.isil.isilapp.utils.API_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ISILAppTheme {
                AppScreen()
            }
        }
    }
}

@Composable
fun AppScreen() {
    var selectedIndex by remember { mutableStateOf(0) }
    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedIndex = selectedIndex,
                onItemClick = { index ->
                    selectedIndex = index
                },
                context = context
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            SearchBar("Search")
            Spacer(modifier = Modifier.height(16.dp))
            LatestNewsSection()
            Spacer(modifier = Modifier.height(16.dp))
            SedesSection(
                onViewAllClick = {
                    context.startActivity(
                        Intent(context, SedesMapActivity::class.java)
                    )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            HotEventsSection()
        }
    }
}


@Composable
fun LatestNewsSection() {
    Text(
        text = "Latest News",
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Black
    )
    Spacer(modifier = Modifier.height(8.dp))

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        NewsCard(
            title = "Título de la primera noticia",
            date = "15 Sep 2025",
            postedBy = "@usuario1",
            imagePainter = painterResource(R.drawable.noticia_uno)
        )

        NewsCard(
            title = "Título de la segunda noticia",
            date = "08 Sep 2025",
            postedBy = "@usuario3",
            imagePainter = painterResource(R.drawable.noticia_dos)
        )
    }
}

// MainActivity.kt (SERVICIO + SECTION)

// Servicio de Retrofit
interface SedesService {
    @GET("sedes.php")
    suspend fun getSedes(): List<Sede>
}

object RetrofitClientSedes {
    private val retrofit: Retrofit = Retrofit
        .Builder()
        .baseUrl(API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService: SedesService = retrofit.create(SedesService::class.java)
}

// Section con lógica de carga
@Composable
fun SedesSection(onViewAllClick: () -> Unit) {
    var listaSedes by remember { mutableStateOf<List<Sede>>(emptyList()) }

    LaunchedEffect(Unit) {
        try {
            listaSedes = RetrofitClientSedes.apiService.getSedes()
        } catch (e: Exception) {
            Log.e("SedesSection", "Error: ${e.message}")
        }
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Sedes",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            TextButton(
                onClick = onViewAllClick,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = "View All")
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Ver todas las sedes",
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(listaSedes) { sede ->
                SedeCard(sede = sede)
            }
        }
    }
}

@Composable
fun HotEventsSection() {
    val context = LocalContext.current

    Text(
        text = "Conoce a nuestros representantes ",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(10.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                val intent = Intent(context, RepresentanteActivity::class.java)
                context.startActivity(intent)
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.inicio_de_ciclo),
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}



