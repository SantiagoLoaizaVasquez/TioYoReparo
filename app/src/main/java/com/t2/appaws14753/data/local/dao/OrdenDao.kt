package com.t2.appaws14753.data.local.dao

import com.t2.appaws14753.data.local.entity.OrdenEntity
import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update


@Dao
interface OrdenDao {
    @Query("SELECT * FROM orden")
    suspend fun getAll(): List<OrdenEntity>

    @Query("SELECT * FROM orden WHERE ordenId = :ordenId")
    suspend fun getById(ordenId: String): OrdenEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: OrdenEntity)

    @Update
    suspend fun update(entity: OrdenEntity)

    @Query("DELETE FROM orden WHERE ordenId = :ordenId")
    suspend fun delete(ordenId: String)
}