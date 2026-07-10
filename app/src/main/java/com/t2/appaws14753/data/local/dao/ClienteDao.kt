package com.t2.appaws14753.data.local.dao

import com.t2.appaws14753.data.local.entity.ClienteEntity
import androidx.room3.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ClienteDao {

    @Query("SELECT * FROM clientes")
    fun getAll(): Flow<List<ClienteEntity>>

    @Query("SELECT * FROM clientes WHERE id = :id")
    suspend fun getById(id: Int): ClienteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ClienteEntity)

    @Update
    suspend fun update(entity: ClienteEntity)

    @Query("DELETE FROM clientes WHERE id = :id")
    suspend fun delete(id: Int)
}