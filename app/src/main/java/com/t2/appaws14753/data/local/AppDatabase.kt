package com.t2.appaws14753.data.local

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.t2.appaws14753.data.local.dao.ClienteDao
import com.t2.appaws14753.data.local.dao.DispositivoDao
import com.t2.appaws14753.data.local.dao.OrdenDao
import com.t2.appaws14753.data.local.dao.ServicioDao
import com.t2.appaws14753.data.local.dao.UsuarioDao
import com.t2.appaws14753.data.local.entity.ClienteEntity
import com.t2.appaws14753.data.local.entity.DispositivoEntity
import com.t2.appaws14753.data.local.entity.OrdenEntity
import com.t2.appaws14753.data.local.entity.ServicioEntity
import com.t2.appaws14753.data.local.entity.UsuarioEntity

@Database(
    entities = [
        ClienteEntity::class,
        DispositivoEntity::class,
        OrdenEntity::class,
        ServicioEntity::class,
        UsuarioEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun clienteDao(): ClienteDao
    abstract fun dispositivoDao(): DispositivoDao
    abstract fun ordenDao(): OrdenDao
    abstract fun servicioDao(): ServicioDao
    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "appaws14753_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
