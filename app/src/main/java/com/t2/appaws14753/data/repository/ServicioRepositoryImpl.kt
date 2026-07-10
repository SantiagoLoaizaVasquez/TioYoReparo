package com.t2.appaws14753.data.repository

import com.t2.appaws14753.data.local.dao.ServicioDao
import com.t2.appaws14753.data.mapper.ServicioMapper
import com.t2.appaws14753.domain.model.Servicio
import com.t2.appaws14753.domain.repository.ServicioRepository

class ServicioRepositoryImpl(private val dao: ServicioDao) : ServicioRepository {

    override suspend fun getAll(): List<Servicio> =
        dao.getAll().map { ServicioMapper.toDomain(it) }

    override suspend fun getById(id: String): Servicio? =
        dao.getById(id)?.let { ServicioMapper.toDomain(it) }

    override suspend fun insert(servicio: Servicio) =
        dao.insert(ServicioMapper.toEntity(servicio))

    override suspend fun update(servicio: Servicio) =
        dao.update(ServicioMapper.toEntity(servicio))

    override suspend fun delete(servicio: Servicio) =
        dao.delete(servicio.servicioId)
}
