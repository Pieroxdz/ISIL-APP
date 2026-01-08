package com.isil.isilapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface RepresentanteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(representante: Representante)

    @Update
    suspend fun actualizar(representante: Representante)

    @Delete
    suspend fun eliminar(representante: Representante)

    @Query("SELECT * FROM representantes")
    fun listar(): Flow<List<Representante>>

    @Query("SELECT * FROM representantes WHERE club_id = :clubId")
    suspend fun listarPorClub(clubId: Int): List<Representante>
}