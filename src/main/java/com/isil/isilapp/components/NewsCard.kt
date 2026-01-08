package com.isil.isilapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun NewsCard(
    title: String,
    date: String? = null,
    postedBy: String? = null,
    imageUrl: String? = null,       // URL de imagen online
    imagePainter: Painter? = null,  // Imagen local
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {

            val imageModifier = Modifier
                .width(110.dp)
                .fillMaxHeight()
                .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))

            when {
                imagePainter != null -> {
                    // Imagen local
                    Image(
                        painter = imagePainter,
                        contentDescription = null,
                        modifier = imageModifier,
                        contentScale = ContentScale.Crop
                    )
                }
                !imageUrl.isNullOrBlank() -> {
                    // Imagen remota con Coil
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        modifier = imageModifier,
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (!date.isNullOrBlank()) {
                    Text(
                        text = date,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                if (!postedBy.isNullOrBlank()) {
                    Text(
                        text = postedBy,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
