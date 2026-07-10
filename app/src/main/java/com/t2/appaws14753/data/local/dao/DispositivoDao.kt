package com.t2.appaws14753.data.local.dao

import com.t2.appaws14753.data.local.entity.DispositivoEntity
import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update


@Dao
interface DispositivoDao {
    @Query("SELECT * FROM dispositivo")
    suspend fun getAll(): List<DispositivoEntity>

    @Query("SELECT * FROM dispositivo WHERE dispositivoId = :id")
    suspend fun getById(id: String): DispositivoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: DispositivoEntity)

    @Update
    suspend fun update(entity: DispositivoEntity)

    @Query("DELETE FROM dispositivo WHERE dispositivoId = :id")
    suspend fun delete(id: String)
}