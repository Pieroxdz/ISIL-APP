package com.isil.isilapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.isil.isilapp.dao.DatabaseProvider
import com.isil.isilapp.dao.Representante
import com.isil.isilapp.dao.RepresentanteDao
import com.isil.isilapp.ui.theme.ISILAppTheme


class RepresentanteActivity : ComponentActivity() {
    private lateinit var representanteDao: RepresentanteDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = DatabaseProvider.getDatabase(applicationContext)
        representanteDao = database.userRepresentante()

        enableEdgeToEdge()
        setContent {
            ISILAppTheme {
                val representanteList = remember { mutableStateOf(listOf<Representante>()) }

                LaunchedEffect(Unit) {
                    representanteDao.listar().collect { representantes ->
                        representanteList.value = representantes
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        FloatingActionButton(onClick = {
                            startActivity(Intent(this@RepresentanteActivity, RepresentanteInsertActivity::class.java))
                        }) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        }
                    }
                ) {
                    Column(modifier = Modifier.padding(it)) {
                        LazyColumn {
                            items(representanteList.value) { representante ->
                                Column(
                                    modifier = Modifier.clickable {
                                        val intent = Intent(this@RepresentanteActivity, RepresentanteUpdateActivity::class.java)
                                        intent.putExtra("id", representante.id)
                                        intent.putExtra("nombre", representante.nombre)
                                        intent.putExtra("correo", representante.correo)
                                        intent.putExtra("telefono", representante.telefono)
                                        intent.putExtra("clubId", representante.clubId)
                                        startActivity(intent)
                                    }.padding(8.dp)
                                ) {
                                    Text(text = "ID: ${representante.id}")
                                    Text(text = "Nombre: ${representante.nombre}")
                                    Text(text = "Correo: ${representante.correo ?: "N/A"}")
                                    Text(text = "Teléfono: ${representante.telefono ?: "N/A"}")
                                    Text(text = "Club ID: ${representante.clubId}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
