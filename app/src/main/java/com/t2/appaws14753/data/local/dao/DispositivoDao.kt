package com.t2.appaws14753.data.local.dao

import com.t2.appaws14753.data.local.entity.DispositivoEntity
import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface DispositivoDao {
    @Query("SELECT * FROM dispositivo")
    fun getAll(): Flow<List<DispositivoEntity>>

    @Query("SELECT * FROM dispositivo WHERE dispositivoId = :dispositivoId")
    suspend fun getById(id: String): DispositivoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: DispositivoEntity)

    @Update
    suspend fun update(entity: DispositivoEntity)

    @Query("DELETE FROM dispositivo WHERE dispositivoId = :dispositivoId")
    suspend fun delete(id: String)
}