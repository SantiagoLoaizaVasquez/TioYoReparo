package com.t2.appaws14753.domain.repository

import com.t2.appaws14753.domain.model.Dispositivo


interface DispositivoRepository {

    suspend fun getAll(): List<Dispositivo>

    suspend fun getById(id: String): Dispositivo?

    suspend fun insert(dispositivo: Dispositivo)

    suspend fun update(dispositivo: Dispositivo)

    suspend fun delete(dispositivo: Dispositivo)
}
