package com.isil.isilapp.components

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.isil.isilapp.models.Sede
import com.isil.isilapp.DetalleSedesActivity

@Composable
fun SedeCard(sede: Sede) {
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable {
            val intent = Intent(context, DetalleSedesActivity::class.java).apply {
                putExtra("SEDE_ID", sede.id)
                putExtra("SEDE_NOMBRE", sede.nombre)
                putExtra("SEDE_IMAGEN_URL", sede.imagen_url)
                // Agrega otros datos que necesites pasar
            }
            context.startActivity(intent)
        }
    ) {
        Box(
            modifier = Modifier
                .size(109.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = sede.imagen_url,
                contentDescription = sede.nombre,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = sede.nombre,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}