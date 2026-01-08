package com.isil.isilapp.models



data class Sede(

    val id: Int,

    val nombre: String,

    val distrito: String,

    val direccion: String,

    val telefono: String? = null,

    val email: String? = null,

    val horario_atencion: String? = null,

    val imagen_url: String? = null,

    val latitud: Double,

    val longitud: Double,

    val descripcion: String? = null,

    val activo: Boolean = true,

    val fecha_creacion: String? = null
)