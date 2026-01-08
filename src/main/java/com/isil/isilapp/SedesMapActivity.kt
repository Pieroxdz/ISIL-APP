package com.isil.isilapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import com.google.maps.android.compose.MarkerState
import com.isil.isilapp.models.Sede
import com.isil.isilapp.ui.theme.ISILAppTheme
import com.isil.isilapp.utils.API_URL

interface SedesApiService {
    @GET("sedes.php")
    suspend fun getSedes(): List<Sede>
}

class SedesMapActivity : ComponentActivity() {

    // Configurar Retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl(API_URL) // 👈 CAMBIA ESTO
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(SedesApiService::class.java)

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ISILAppTheme {
                var listaSedes by remember { mutableStateOf<List<Sede>>(emptyList()) }
                var isLoading by remember { mutableStateOf(true) }
                var errorMessage by remember { mutableStateOf<String?>(null) }
                val coroutineScope = rememberCoroutineScope()

                // Cargar sedes desde el web service
                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        try {
                            Log.d("SedesMapActivity", "Cargando sedes...")
                            val sedes = apiService.getSedes()

                            // Filtrar solo sedes con coordenadas válidas
                            listaSedes = sedes.filter {
                                it.latitud != 0.0 && it.longitud != 0.0
                            }

                            isLoading = false

                            Log.d("SedesMapActivity", "✅ Sedes cargadas: ${listaSedes.size}")
                            listaSedes.forEach { sede ->
                                Log.d("SedesMapActivity",
                                    "📍 ${sede.nombre} - Lat: ${sede.latitud}, Lng: ${sede.longitud}")
                            }

                        } catch (e: Exception) {
                            Log.e("SedesMapActivity", "❌ Error: ${e.message}", e)
                            errorMessage = e.message
                            isLoading = false

                            Toast.makeText(
                                this@SedesMapActivity,
                                "Error al cargar sedes: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Column {
                                    Text("Sedes ISIL")
                                    if (listaSedes.isNotEmpty()) {
                                        Text(
                                            text = "${listaSedes.size} sedes encontradas",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Volver"
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                ) { paddingValues ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        when {
                            isLoading -> {
                                // Mostrar loading
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }

                            errorMessage != null -> {
                                // Mostrar error
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "⚠️ Error al cargar sedes",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = errorMessage ?: "Error desconocido",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(onClick = { finish() }) {
                                        Text("Volver")
                                    }
                                }
                            }

                            listaSedes.isEmpty() -> {
                                // No hay sedes
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "📍 No se encontraron sedes",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Verifica tu conexión a internet",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            else -> {
                                // Mostrar mapa con sedes
                                DibujarMapaConSedes(sedes = listaSedes)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DibujarMapaConSedes(sedes: List<Sede>) {
    // Calcular el centro del mapa (promedio de todas las coordenadas)
    val centroMapa = remember(sedes) {
        if (sedes.isNotEmpty()) {
            val latitudPromedio = sedes.map { it.latitud }.average()
            val longitudPromedio = sedes.map { it.longitud }.average()
            LatLng(latitudPromedio, longitudPromedio)
        } else {
            LatLng(-12.0464, -77.0428) // Lima por defecto
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(centroMapa, 12f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        // Dibujar un marker por cada sede
        sedes.forEach { sede ->
            val posicion = LatLng(sede.latitud, sede.longitud)

            Marker(
                state = rememberMarkerState(position = posicion),
                title = sede.nombre,
                snippet = buildString {
                    append("📍 ${sede.direccion}")
                    append("\n🏙️ ${sede.distrito}")
                },
                icon = BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_AZURE // Marker azul
                ),
                onClick = { marker ->
                    marker.showInfoWindow()
                    true
                }
            )
        }
    }
}