package com.isil.isilapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.isil.isilapp.components.BottomNavigationBar
import com.isil.isilapp.ui.theme.ISILAppTheme

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ISILAppTheme {
                ProfileContent()
            }
        }
    }
}

val profileIcons = mapOf(
    "Messages" to Icons.Default.Email,
    "View History" to Icons.Default.History,
    "Saved" to Icons.Default.Bookmark,
    "My Account" to Icons.Default.Person
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileContent() {
    var selectedIndex by remember { mutableStateOf(4) } // 4 = Profile
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(
                selectedIndex = selectedIndex,
                onItemClick = { index -> selectedIndex = index },
                context = context
            )
        }
    ) {

        Column(
            modifier = Modifier
//                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // 🔹 Column superior (solo título)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF0F0F0)) // Gris claro
                    .padding(dimensionResource(R.dimen.space_3))
            ) {
                Text(
                    text = stringResource(R.string.title_activity_profile),
                    style = MaterialTheme.typography.headlineLarge
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_2)))

            // 🔹 Column inferior (perfil + opciones)
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Imagen de perfil
                Image(
                    painter = painterResource(id = R.drawable.ic_profile_placeholder),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Nombre
                Text(
                    text = "Markin Piero",
                    style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Subtítulo
                Text(
                    text = "Estudiante de Desarrollo de Software",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Normal
                    ),
                    color = Color(0xFF757575),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Opciones dinámicas
                profileIcons.forEach { (label, icon) ->
                    ProfileOptionItem(
                        text = label,
                        icon = icon,
                        onClick = {
                            when (label) {
                                "Messages" -> { /* Navegar al chat */ }
                                "View History" -> { /* Navegar a historial */ }
                                "Saved" -> { /* Navegar a guardados */ }
                                "My Account" -> { /* Ir a MyAccountActivity */ }
                            }
                        }
                    )
                }
            }
        }
    }
}



@Composable
fun ProfileOptionItem(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Ícono azul al inicio
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary // azul de tu tema
        )
        Spacer(modifier = Modifier.width(12.dp))

        // Texto
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        // Flecha al final
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}