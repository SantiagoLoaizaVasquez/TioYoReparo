package com.t2.appaws14753.domain.repository

import com.t2.appaws14753.domain.model.Servicio


interface ServicioRepository {

    suspend fun getAll(): List<Servicio>

    suspend fun getById(id: String): Servicio?

    suspend fun insert(servicio: Servicio)

    suspend fun update(servicio: Servicio)

    suspend fun delete(servicio: Servicio)
}
