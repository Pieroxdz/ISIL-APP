package com.isil.isilapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.isil.isilapp.R


// Definir la familia de fuentes Avenir
val avenirFontFamily = FontFamily(
    Font(R.font.avenir_black, FontWeight.Black),
    Font(R.font.avenir_heavy, FontWeight.ExtraBold),
    Font(R.font.avenir_medium, FontWeight.Medium),
    Font(R.font.avenir_roman, FontWeight.Normal),
    Font(R.font.avenir_book, FontWeight.Light),
    Font(R.font.avenir_light_oblique, FontWeight.Light, FontStyle.Italic)
)

// Typography según las especificaciones exactas del diseñador
val Typography = Typography(
    // Avenir Black - 28pt
    headlineLarge = TextStyle(
        fontFamily = avenirFontFamily,
        fontWeight = FontWeight.Black,
        fontSize = 28.sp,
        lineHeight = 34.sp,
        letterSpacing = 0.sp
    ),

    // Avenir Heavy - 18pt
    headlineMedium = TextStyle(
        fontFamily = avenirFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),

    // Avenir Medium - 17pt
    headlineSmall = TextStyle(
        fontFamily = avenirFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 17.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.sp
    ),

    // Avenir Roman - 14pt
    bodyLarge = TextStyle(
        fontFamily = avenirFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp
    ),

    // Avenir Book - 14pt
    bodyMedium = TextStyle(
        fontFamily = avenirFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),

    // Avenir Light Oblique - 13pt
    bodySmall = TextStyle(
        fontFamily = avenirFontFamily,
        fontWeight = FontWeight.Light,
        fontStyle = FontStyle.Italic,
        fontSize = 13.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    )
)