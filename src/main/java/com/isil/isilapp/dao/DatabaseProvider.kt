package com.isil.isilapp.dao

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    private var INSTANCE: RepresentanteDatabase? = null

    fun getDatabase(context: Context): RepresentanteDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                RepresentanteDatabase::class.java,
                "representante_database"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}
