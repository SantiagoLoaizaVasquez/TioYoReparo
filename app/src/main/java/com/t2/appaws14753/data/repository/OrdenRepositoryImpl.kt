package com.t2.appaws14753.data.repository

import com.t2.appaws14753.data.local.dao.OrdenDao
import com.t2.appaws14753.data.mapper.OrdenMapper
import com.t2.appaws14753.domain.model.Orden
import com.t2.appaws14753.domain.repository.OrdenRepository

class OrdenRepositoryImpl(private val dao: OrdenDao) : OrdenRepository {

    override suspend fun getAll(): List<Orden> =
        dao.getAll().map { OrdenMapper.toDomain(it) }

    override suspend fun getById(id: String): Orden? =
        dao.getById(id)?.let { OrdenMapper.toDomain(it) }

    override suspend fun insert(orden: Orden) =
        dao.insert(OrdenMapper.toEntity(orden))

    override suspend fun update(orden: Orden) =
        dao.update(OrdenMapper.toEntity(orden))

    override suspend fun delete(orden: Orden) =
        dao.delete(orden.ordenId)
}
