package com.t2.appaws14753.data.repository

import com.t2.appaws14753.data.mapper.toEntity
import com.t2.appaws14753.data.mapper.toDomain
import com.t2.appaws14753.domain.model.Cliente
import com.t2.appaws14753.domain.repository.ClienteRepository
import com.t2.appaws14753.data.local.dao.ClienteDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ClienteRepositoryImpl(private val dao: ClienteDao) : ClienteRepository {
    override fun getAll(): Flow<List<Cliente>> = dao.getAll().map { list -> list.map { it.toDomain() } }
    override suspend fun getById(id: Int) = dao.getById(id)?.toDomain()
    override suspend fun insert(cliente: Cliente) = dao.insert(cliente.toEntity())
    override suspend fun update(cliente: Cliente) = dao.update(cliente.toEntity())
    override suspend fun delete(id: Int) = dao.delete(id)
}