package com.t2.appaws14753.data.local.dao

import com.t2.appaws14753.data.local.entity.ServicioEntity
import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update


@Dao
interface ServicioDao {
    @Query("SELECT * FROM servicio")
    suspend fun getAll(): List<ServicioEntity>

    @Query("SELECT * FROM servicio WHERE servicioId = :servicioId")
    suspend fun getById(servicioId: String): ServicioEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ServicioEntity)

    @Update
    suspend fun update(entity: ServicioEntity)

    @Query("DELETE FROM servicio WHERE servicioId = :servicioId")
    suspend fun delete(servicioId: String)
}