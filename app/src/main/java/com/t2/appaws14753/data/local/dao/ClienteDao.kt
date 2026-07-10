package com.t2.appaws14753.data.local.dao

import com.t2.appaws14753.data.local.entity.ClienteEntity
import androidx.room3.*

@Dao
interface ClienteDao {

    @Query("SELECT * FROM clientes")
    suspend fun getAll(): List<ClienteEntity>

    @Query("SELECT * FROM clientes WHERE id = :id")
    suspend fun getById(id: Int): ClienteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ClienteEntity)

    @Update
    suspend fun update(entity: ClienteEntity)

    @Query("DELETE FROM clientes WHERE id = :id")
    suspend fun delete(id: String)
}