package com.t2.appaws14753.domain.repository
import com.t2.appaws14753.domain.model.Cliente

interface ClienteRepository {

    suspend fun getAll(): List<Cliente>
    suspend fun getById(id: String): Cliente?
    suspend fun insert(cliente: Cliente)
    suspend fun update(cliente: Cliente)
    suspend fun delete(cliente: Cliente)
}