package com.isil.isilapp.models


data class SedeDetalle(
    val id: Int,
    val nombre: String,
    val distrito: String,
    val direccion: String,
    val telefono: String,
    val email: String,
    val horario_atencion: String,
    val imagen_url: String,
    val latitud: String,
    val longitud: String,
    val descripcion: String,
    val activo: Int
)