package com.isil.isilapp.dao

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Representante::class], version = 1, exportSchema = false)
abstract class RepresentanteDatabase:  RoomDatabase() {
    abstract fun userRepresentante(): RepresentanteDao

    companion object
}