package com.t2.appaws14753.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update
import com.t2.appaws14753.data.local.entity.OrdenEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface OrdenDao {
    @Query("SELECT * FROM orden")
    fun getAll(): Flow<List<OrdenEntity>>

    @Query("SELECT * FROM orden WHERE ordenId = :ordenId")
    suspend fun getById(ordenId: String): OrdenEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: OrdenEntity)

    @Update
    suspend fun update(entity: OrdenEntity)

    @Query("DELETE FROM orden WHERE ordenId = :ordenId")
    suspend fun delete(ordenId: String)
}