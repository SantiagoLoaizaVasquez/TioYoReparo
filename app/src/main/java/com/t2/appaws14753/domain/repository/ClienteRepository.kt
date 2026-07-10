package com.t2.appaws14753.domain.repository
import com.t2.appaws14753.domain.model.Cliente
import kotlinx.coroutines.flow.Flow

interface ClienteRepository {

    fun getAll(): Flow<List<Cliente>>
    suspend fun getById(id: Int): Cliente?
    suspend fun insert(cliente: Cliente)
    suspend fun update(cliente: Cliente)
    suspend fun delete(id: Int)
}