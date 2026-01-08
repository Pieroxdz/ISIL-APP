package com.isil.isilapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.isil.isilapp.models.Club
import com.isil.isilapp.ui.theme.ISILAppTheme
import com.isil.isilapp.utils.API_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ClubDetalleService {
    @GET("club_detalle.php")
    suspend fun getClubDetalle(@Query("idclub") idclub: Int): List<Club>
}

object RetrofitClientClubDetalle {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ClubDetalleService = retrofit.create(ClubDetalleService::class.java)
}

class DetalleClubsActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val idclub = intent.getIntExtra("idclub", 0)
        val nombreClub = intent.getStringExtra("nombre") ?: ""

        setContent {
            ISILAppTheme {
                var isLoading by remember { mutableStateOf(true) }
                var clubSeleccionado by remember { mutableStateOf<Club?>(null) }

                LaunchedEffect(Unit) {
                    clubSeleccionado = RetrofitClientClubDetalle.apiService.getClubDetalle(idclub).firstOrNull()
                    isLoading = false
                }

                if (isLoading) {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text(text = nombreClub) },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    titleContentColor = Color.White
                                ),
                                navigationIcon = {
                                    IconButton(onClick = { finish() }) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = "Volver",
                                            tint = Color.White
                                        )
                                    }
                                }
                            )
                        }
                    ) { innerPadding ->
                        Column(
                            Modifier
                                .padding(innerPadding)
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = dimensionResource(R.dimen.space_1))
                        ) {
                            clubSeleccionado?.let { club ->
                                // Imagen principal
                                AsyncImage(
                                    model = club.imagen_url,
                                    contentDescription = club.nombre,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(240.dp),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                // Título del club
                                Text(
                                    text = club.nombre,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                HorizontalDivider(thickness = 1.dp)

                                Spacer(modifier = Modifier.height(16.dp))

                                // Categoría
                                FilaDetalle("Categoría", club.nombre ?: "Sin categoría")

                                Spacer(modifier = Modifier.height(16.dp))

                                // Descripción
                                Text(
                                    text = "Descripción",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = club.descripcion ?: "Sin descripción disponible",
                                    style = MaterialTheme.typography.bodyLarge
                                )

                                Spacer(modifier = Modifier.height(24.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilaDetalle(etiqueta: String, valor: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = etiqueta,
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = valor,
            modifier = Modifier.weight(2f),
            style = MaterialTheme.typography.bodyLarge
        )
    }
    HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
}