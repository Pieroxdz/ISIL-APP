package com.isil.isilapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.isil.isilapp.components.BottomNavigationBar
import com.isil.isilapp.components.ClubCard
import com.isil.isilapp.models.Club
import com.isil.isilapp.ui.theme.ISILAppTheme
import com.isil.isilapp.utils.API_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Interface del servicio
interface ClubsPorCategoriaService {
    @GET("categorias_clubes.php")
    suspend fun getClubesPorCategoria(@Query("idcategoria") idCategoria: Int): List<Club>
}

object RetrofitClientClubesPorCategoria {
    private val retrofit: Retrofit = Retrofit
        .Builder()
        .baseUrl(API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService: ClubsPorCategoriaService = retrofit.create(ClubsPorCategoriaService::class.java)
}

class ClubsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val idCategoria = intent.getIntExtra("idcategoria", 0)
        val nombreCategoria = intent.getStringExtra("nombre") ?: ""

        setContent {
            ISILAppTheme {
                ClubsPorCategoriaContent(
                    idCategoria = idCategoria,
                    nombreCategoria = nombreCategoria
                )
            }
        }
    }
}

@Composable
fun ClubsPorCategoriaContent(idCategoria: Int, nombreCategoria: String) {
    var selectedIndex by remember { mutableStateOf(2) }
    val context = LocalContext.current

    var isLoading by remember { mutableStateOf(true) }
    var listaClubes by remember { mutableStateOf<List<Club>?>(null) }

    LaunchedEffect(Unit) {
        listaClubes = RetrofitClientClubesPorCategoria.apiService.getClubesPorCategoria(idCategoria)
        isLoading = false
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(
                selectedIndex = selectedIndex,
                onItemClick = { index -> selectedIndex = index },
                context = context
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Text(
                text = nombreCategoria,
                modifier = Modifier.padding(
                    start = dimensionResource(R.dimen.space_1),
                    top = dimensionResource(R.dimen.space_1),
                    end = dimensionResource(R.dimen.space_1)
                ),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(R.dimen.space_1))
                )
            } else {
                if (listaClubes.isNullOrEmpty()) {
                    Text(
                        text = "No hay clubes en esta categoría",
                        modifier = Modifier.padding(dimensionResource(R.dimen.space_1)),
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(1),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(listaClubes!!) { club ->
                            ClubCard(
                                nombre = club.nombre,
                                categoria = nombreCategoria,
                                imagenUrl = club.imagen_url ?: "",
                                onClick = {
                                    val intent = Intent(context, DetalleClubsActivity::class.java)
                                    intent.putExtra("idclub", club.id)
                                    intent.putExtra("nombre", club.nombre)
                                    context.startActivity(intent)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}