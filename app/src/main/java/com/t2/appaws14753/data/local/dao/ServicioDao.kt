package com.t2.appaws14753.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update

import com.t2.appaws14753.data.local.entity.ServicioEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ServicioDao {
    @Query("SELECT * FROM servicio")
    fun getAll(): Flow<List<ServicioEntity>>

    @Query("SELECT * FROM servicio WHERE servicioId = :servicioId")
    suspend fun getById(servicioId: String): ServicioEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ServicioEntity)

    @Update
    suspend fun update(entity: ServicioEntity)

    @Query("DELETE FROM servicio WHERE servicioId = :servicioId")
    suspend fun delete(servicioId: String)
}