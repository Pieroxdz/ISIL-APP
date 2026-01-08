package com.isil.isilapp.dao

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "representantes")
data class Representante(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val correo: String?,       // Opcional
    val telefono: String?,     // Opcional
    @ColumnInfo(name = "club_id") val clubId: Int // Id del club desde tu servidor
)