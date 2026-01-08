package com.isil.isilapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.isil.isilapp.dao.DatabaseProvider
import com.isil.isilapp.dao.Representante
import com.isil.isilapp.ui.theme.ISILAppTheme

import kotlinx.coroutines.launch

class RepresentanteUpdateActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent.extras!!
        val id = bundle.getInt("id")
        val nombreInicial = bundle.getString("nombre").orEmpty()
        val correoInicial = bundle.getString("correo").orEmpty()
        val telefonoInicial = bundle.getString("telefono").orEmpty()
        val clubIdInicial = bundle.getInt("clubId").toString()

        enableEdgeToEdge()
        setContent {
            var nombre by remember { mutableStateOf(nombreInicial) }
            var correo by remember { mutableStateOf(correoInicial) }
            var telefono by remember { mutableStateOf(telefonoInicial) }
            var clubId by remember { mutableStateOf(clubIdInicial) }

            ISILAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding).padding(32.dp)) {
                        Text(text = "Editar Representante", style = MaterialTheme.typography.headlineLarge)

                        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.padding(8.dp))
                        OutlinedTextField(value = correo, onValueChange = { correo = it }, label = { Text("Correo") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.padding(8.dp))
                        OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.padding(8.dp))
                        OutlinedTextField(value = clubId, onValueChange = { clubId = it }, label = { Text("Club ID") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.padding(16.dp))

                        OutlinedButton(onClick = {
                            val db = DatabaseProvider.getDatabase(this@RepresentanteUpdateActivity)
                            val dao = db.userRepresentante()
                            lifecycleScope.launch {
                                val representante = Representante(
                                    id = id,
                                    nombre = nombre,
                                    correo = correo.ifEmpty { null },
                                    telefono = telefono.ifEmpty { null },
                                    clubId = clubId.toInt()
                                )
                                dao.actualizar(representante)
                                finish()
                            }
                        }) {
                            Text("Guardar cambios")
                        }
                    }
                }
            }
        }
    }
}
