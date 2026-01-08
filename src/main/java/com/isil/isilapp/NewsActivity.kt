package com.isil.isilapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.isil.isilapp.components.NewsCard
import com.isil.isilapp.ui.theme.ISILAppTheme
import com.isil.isilapp.utils.API_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import com.isil.isilapp.models.Noticia

import com.isil.isilapp.components.BottomNavigationBar

interface NoticiasService{
    @GET("noticias.php")
    suspend fun getNoticia(): List<Noticia>
}

class NewsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ISILAppTheme {
                NewsContent()
            }
        }
    }
}

//Usamos la libreria retrofit para traer datos
val API = Retrofit.Builder()
    .baseUrl(API_URL) //Nos comunicamos con el WS
    .addConverterFactory(GsonConverterFactory.create()) //Convertimos a JSON
    .build() //Compilamos
    .create(NoticiasService::class.java)


@Composable
fun NewsContent() {
    var selectedIndex by remember { mutableStateOf(3) } // 3 = News
    val context = LocalContext.current


    var isLoading by remember {
        mutableStateOf(
            true
        )
    }

    var listaNoticias by remember {
        mutableStateOf<List<Noticia>?>(null)
    }

    LaunchedEffect(Unit) {
        listaNoticias = API.getNoticia()
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
            modifier = Modifier.padding(innerPadding)
        ) {
            Text(
                text = stringResource(R.string.title_activity_news),
                modifier = Modifier.padding(dimensionResource(R.dimen.space_1)),
                style = MaterialTheme.typography.headlineLarge
            )
            if (isLoading) {
                LinearProgressIndicator()
            } else {
                LazyColumn {
                    items(listaNoticias.orEmpty()) { noticia ->
                        NewsCard(
                            title = noticia.titulo,
                            postedBy = noticia.autor,
                            imageUrl = noticia.imagen_url,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

