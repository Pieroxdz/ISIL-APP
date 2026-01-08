package com.isil.isilapp.models

data class Club(
    val id: Int,
    val nombre: String,
    val descripcion: String?,
    val imagen_url: String?,
    val idcategoria: Int
)
