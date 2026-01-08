package com.isil.isilapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.isil.isilapp.dao.DatabaseProvider
import com.isil.isilapp.dao.Representante
import com.isil.isilapp.ui.theme.ISILAppTheme
import kotlinx.coroutines.launch

class RepresentanteInsertActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var nombre = remember { mutableStateOf("") }
            var correo = remember { mutableStateOf("") }
            var telefono = remember { mutableStateOf("") }
            var clubId = remember { mutableStateOf("") }

            ISILAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding).padding(32.dp)) {
                        Text(
                            text = "Nuevo Representante",
                            style = MaterialTheme.typography.headlineLarge
                        )
                        OutlinedTextField(
                            value = nombre.value, onValueChange = { nombre.value = it },
                            label = { Text("Nombre") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        OutlinedTextField(
                            value = correo.value, onValueChange = { correo.value = it },
                            label = { Text("Correo") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        OutlinedTextField(
                            value = telefono.value, onValueChange = { telefono.value = it },
                            label = { Text("Teléfono") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        OutlinedTextField(
                            value = clubId.value, onValueChange = { clubId.value = it },
                            label = { Text("ID del Club") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.padding(16.dp))
                        OutlinedButton(onClick = {
                            val db = DatabaseProvider.getDatabase(this@RepresentanteInsertActivity)
                            val dao = db.userRepresentante()
                            lifecycleScope.launch {
                                val representante = Representante(
                                    nombre = nombre.value,
                                    correo = correo.value.ifEmpty { null },
                                    telefono = telefono.value.ifEmpty { null },
                                    clubId = clubId.value.toInt()
                                )
                                dao.insertar(representante)
                                finish()
                            }
                        }) {
                            Text("Guardar")
                        }
                    }
                }
            }
        }
    }
}
