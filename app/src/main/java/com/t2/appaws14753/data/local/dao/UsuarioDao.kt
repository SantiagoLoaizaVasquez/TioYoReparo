package com.t2.appaws14753.data.local.dao

import com.t2.appaws14753.data.local.entity.UsuarioEntity
import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuario")
    suspend fun getAll(): List<UsuarioEntity>

    @Query("SELECT * FROM usuario WHERE usuarioId = :usuarioId")
    suspend fun getById(usuarioId: String): UsuarioEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: UsuarioEntity)

    @Update
    suspend fun update(entity: UsuarioEntity)

    @Query("DELETE FROM usuario WHERE usuarioId = :usuarioId")
    suspend fun delete(usuarioId: String)
}