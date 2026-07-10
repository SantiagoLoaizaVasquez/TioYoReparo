package com.t2.appaws14753.data.repository

import com.t2.appaws14753.data.local.dao.DispositivoDao
import com.t2.appaws14753.data.mapper.DispositivoMapper
import com.t2.appaws14753.domain.model.Dispositivo
import com.t2.appaws14753.domain.repository.DispositivoRepository

class DispositivoRepositoryImpl(private val dao: DispositivoDao) : DispositivoRepository {

    override suspend fun getAll(): List<Dispositivo> =
        dao.getAll().map { DispositivoMapper.ToDomain(it) }

    override suspend fun getById(id: String): Dispositivo? =
        dao.getById(id)?.let { DispositivoMapper.ToDomain(it) }

    override suspend fun insert(dispositivo: Dispositivo) =
        dao.insert(DispositivoMapper.ToEntity(dispositivo))

    override suspend fun update(dispositivo: Dispositivo) =
        dao.update(DispositivoMapper.ToEntity(dispositivo))

    override suspend fun delete(dispositivo: Dispositivo) =
        dao.delete(dispositivo.dispositivoId)
}
