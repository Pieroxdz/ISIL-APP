package com.isil.isilapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.isil.isilapp.components.BottomNavigationBar
import com.isil.isilapp.models.CategoriaClub
import com.isil.isilapp.ui.theme.ISILAppTheme
import com.isil.isilapp.utils.API_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// Interface del servicio
interface CategoriasClubsService {
    @GET("categorias.php")
    suspend fun getCategoriasClubs(): List<CategoriaClub>
}

object RetrofitClientCategoriasClubs {
    private val retrofit: Retrofit = Retrofit
        .Builder()
        .baseUrl(API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService: CategoriasClubsService = retrofit.create(CategoriasClubsService::class.java)
}

class CategoriasClubsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ISILAppTheme {
                CategoriasClubsContent(
                    onCategoriaClick = { categoria ->
                        seleccionarCategoria(categoria)
                    }
                )
            }
        }
    }

    private fun seleccionarCategoria(categoria: CategoriaClub) {
        Log.d("Categoria", categoria.nombre)
        Toast.makeText(this, categoria.nombre, Toast.LENGTH_SHORT).show()

        val bundle = Bundle().apply {
            putInt("idcategoria", categoria.id)
            putString("nombre", categoria.nombre)
            putString("descripcion", categoria.descripcion)
        }
        val intent = Intent(this, ClubsActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}

@Composable
fun CategoriasClubsContent(onCategoriaClick: (CategoriaClub) -> Unit) {
    var selectedIndex by remember { mutableStateOf(2) } // 2 = Organizations
    val context = LocalContext.current

    var isLoading by remember { mutableStateOf(true) }
    var listaCategorias by remember { mutableStateOf<List<CategoriaClub>?>(null) }

    LaunchedEffect(Unit) {
        listaCategorias = RetrofitClientCategoriasClubs.apiService.getCategoriasClubs()
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
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Categorías de Clubes",
                modifier = Modifier.padding(dimensionResource(R.dimen.space_1)),
                style = MaterialTheme.typography.headlineLarge
            )

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                LazyColumn {
                    items(listaCategorias.orEmpty()) { categoria ->
                        Card(
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = dimensionResource(R.dimen.space_1)
                            ),
                            modifier = Modifier
                                .padding(dimensionResource(R.dimen.space_1))
                                .fillMaxWidth()
                                .clickable {
                                    onCategoriaClick(categoria)
                                }
                        ) {
                            DibujarCategoriaClub(categoria)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DibujarCategoriaClub(categoria: CategoriaClub) {
    AsyncImage(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        model = categoria.imagen_url,
        contentScale = ContentScale.Crop,
        contentDescription = categoria.nombre
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.space_2))
    ) {
        Text(
            text = categoria.id.toString(),
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier.width(50.dp)
        )
        Column {
            Text(
                categoria.nombre,
                style = MaterialTheme.typography.titleLarge
            )
            Text(categoria.descripcion.toString())
        }
    }
}