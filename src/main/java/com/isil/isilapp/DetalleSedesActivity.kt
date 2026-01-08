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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.google.gson.annotations.SerializedName
import com.isil.isilapp.models.SedeDetalle
import com.isil.isilapp.ui.theme.ISILAppTheme
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query



// ==================== API ====================
interface SedeApi {
    @GET("sedes_detalle.php")
    suspend fun getSedeDetalle(@Query("idsede") idSede: Int): List<SedeDetalle>
}

object SedeService {
    private const val BASE_URL = "https://markin.alwaysdata.net/WS_ISIL/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: SedeApi = retrofit.create(SedeApi::class.java)
}

// ==================== ACTIVITY ====================
class DetalleSedesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sedeId = intent.getIntExtra("SEDE_ID", 1)

        setContent {
            ISILAppTheme {
                DetalleSedesScreen(
                    sedeId = sedeId,
                    onBackClick = { finish() }
                )
            }
        }
    }
}

// ==================== COMPOSABLES ====================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleSedesScreen(sedeId: Int, onBackClick: () -> Unit) {
    var sedeDetalle by remember { mutableStateOf<SedeDetalle?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(sedeId) {
        scope.launch {
            try {
                isLoading = true
                errorMessage = null
                val response = SedeService.api.getSedeDetalle(sedeId)
                sedeDetalle = response.firstOrNull()
                if (sedeDetalle == null) {
                    errorMessage = "No se encontró información de la sede"
                }
            } catch (e: Exception) {
                errorMessage = "Error al cargar datos: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Sede") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = errorMessage ?: "Error desconocido",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
            sedeDetalle != null -> {
                SedeDetalleContent(
                    sede = sedeDetalle!!,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
fun SedeDetalleContent(sede: SedeDetalle, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Imagen principal
        AsyncImage(
            model = sede.imagen_url,
            contentDescription = sede.nombre,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Nombre de la sede
            Text(
                text = sede.nombre,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = sede.distrito,
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Descripción
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Descripción",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = sede.descripcion,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Información de contacto
            InfoCard(
                title = "Información de Contacto",
                items = listOf(
                    InfoItem(Icons.Default.LocationOn, "Dirección", sede.direccion),
                    InfoItem(Icons.Default.Phone, "Teléfono", sede.telefono),
                    InfoItem(Icons.Default.Email, "Email", sede.email),
                    InfoItem(Icons.Default.AccessTime, "Horario", sede.horario_atencion)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Ubicación
            InfoCard(
                title = "Ubicación GPS",
                items = listOf(
                    InfoItem(Icons.Default.Place, "Latitud", sede.latitud),
                    InfoItem(Icons.Default.Place, "Longitud", sede.longitud)
                )
            )

        }
    }
}

@Composable
fun InfoCard(title: String, items: List<InfoItem>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))

            items.forEach { item ->
                InfoRow(item)
                if (item != items.last()) {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun InfoRow(item: InfoItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.label,
                fontSize = 12.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = item.value,
                fontSize = 14.sp,
                lineHeight = 18.sp
            )
        }
    }
}

data class InfoItem(
    val icon: ImageVector,
    val label: String,
    val value: String
)